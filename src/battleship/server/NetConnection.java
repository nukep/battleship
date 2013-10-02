package battleship.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import battleship.common.GameSettings;
import battleship.common.MessageToClient;
import battleship.common.Player;
import battleship.netmessages.MessageNetClient;
import battleship.netmessages.MessageNetServer;
import battleship.netmessages.client.NetClientChat;
import battleship.netmessages.client.NetClientDisconnected;
import battleship.netmessages.client.NetClientGameComplete;
import battleship.netmessages.client.NetClientHitMiss;
import battleship.netmessages.client.NetClientOpponentJoin;
import battleship.netmessages.client.NetClientOpponentStrike;
import battleship.netmessages.client.NetClientSettings;
import battleship.netmessages.client.NetClientTurn;
import battleship.netmessages.server.NetServerConnect;
import battleship.netmessages.server.NetServerDisconnect;

/**
 * The NetConnection class is a socket connection and its associated
 * input/output threads.
 *
 */
class NetConnection {    
    private static class NetConnectionM2C implements MessageToClient
    {
        private BlockingQueue<MessageNetClient> queue;
        
        public NetConnectionM2C(BlockingQueue<MessageNetClient> queue)
        {
            this.queue = queue;
        }
        
        @Override
        public void opponentJoin(String name) {
            queue.add(new NetClientOpponentJoin(name));
        }

        @Override
        public void disconnected(boolean opponentLeft) {
            queue.add(new NetClientDisconnected(opponentLeft));
        }

        @Override
        public void firstTurn(boolean yourTurn) {
            queue.add(new NetClientTurn(yourTurn));
        }

        @Override
        public void chat(String message, Date date) {
            queue.add(new NetClientChat(message, date));
        }

        @Override
        public void hitMiss(boolean hit, boolean shipSunk) {
            queue.add(new NetClientHitMiss(hit, shipSunk));
        }

        @Override
        public void opponentStrike(int x, int y) {
            queue.add(new NetClientOpponentStrike(x, y));
        }

        @Override
        public void gameComplete(boolean youWin) {
            queue.add(new NetClientGameComplete(youWin));
        }

        @Override
        public void settings(GameSettings settings) {
            queue.add(new NetClientSettings(settings));
        }
    }
    
    private Socket socket;
    private Thread inThread, outThread;
    private InputRunnable inputRunnable;
    private BlockingQueue<MessageNetClient> outputQueue;
    private NetMatchHandle matchHandle;
    private MessageToClient m2c;

    public NetConnection(Socket socket, NetMatchmaker matchmaker)
    {
        this.socket = socket;
        this.outputQueue = new LinkedBlockingQueue<>();
        this.matchHandle = new NetMatchHandle(matchmaker, this);
        this.m2c = new NetConnectionM2C(outputQueue);
    }
    
    public void start()
    {
        inputRunnable = new InputRunnable(socket, matchHandle);
        inThread = new Thread(inputRunnable,
                              matchHandle.getID() + " Input");
        outThread = new Thread(new OutputRunnable(socket, outputQueue),
                               matchHandle.getID() + " Output");
        
        inThread.start();
        outThread.start();
    }
    
    public void close()
    {
        // unblock any IO read/write methods
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (IOException e) {}  // silently close
        
        // unblock any thread-blocking methods (e.g. wait, sleep, join)
        // inThread cannot be interrupted
        outThread.interrupt();
    }
    
    public void stopAndWait() throws InterruptedException
    {
        close();
        inThread.join();
        outThread.join();
    }
    
    public MessageToClient getMessageToClient()
    {
        return m2c;
    }
}

class NetMatchHandle {
    private NetMatchmaker matchmaker;
    private NetConnection conn;
    
    public NetMatchHandle(NetMatchmaker matchmaker, NetConnection conn)
    {
        this.matchmaker = matchmaker;
        this.conn = conn;
    }

    public int getID()
    {
        return matchmaker.getID(conn);
    }

    public NetGamePlayerHandle connect(Player player)
            throws InterruptedException
    {
        return matchmaker.connect(conn, player);
    }
    
    public void disconnect()
    {
        matchmaker.disconnect(conn);
    }
}

class InputRunnable implements Runnable {
    /** A separate message thread that reads from a BlockingQueue.
     * We need this so that only socket reads can block this thread.
     * Otherwise, we can't know if the socket closes if something else
     * (such as wait()) blocks this thread.
     */
    private class InputMessageRunnable implements Runnable {
        @Override
        public void run()
        {
            try {
                NetGamePlayerHandle playerHandle;
                
                NetServerConnect connectMessage;
                connectMessage = (NetServerConnect)messageQueue.take();
                
                playerHandle = matchHandle.connect(connectMessage.toPlayer());
                playerHandle.messageToServer(connectMessage);
                
                while (true) {
                    MessageNetServer message = messageQueue.take();
                    playerHandle.messageToServer(message);
                }
            } catch (InterruptedException e) {
                // connection closed
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private Socket socket;
    private NetMatchHandle matchHandle;
    private BlockingQueue<MessageNetServer> messageQueue;

    public InputRunnable(Socket socket, NetMatchHandle matchHandle)
    {
        this.socket = socket;
        this.matchHandle = matchHandle;
        this.messageQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void run()
    {
        Thread messageThread = new Thread(new InputMessageRunnable());
        messageThread.start();
        
        try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                MessageNetServer message;
                message = (MessageNetServer)ois.readObject();
                
                if (message instanceof NetServerDisconnect) {
                    break;
                }

                messageQueue.add(message);
            }
        } catch (EOFException|SocketException e) {
            // connection has ended
        } catch (IOException e) {
            System.err.println("Server IO error (" + socket.getInetAddress() + "): " + e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            // might happen if read object is of the wrong class type
            e.printStackTrace();
        }
        
        // close the socket
        try {
            socket.close();
        } catch (IOException e) {}
        
        // tell the message thread to stop taking from the queue and to die
        messageThread.interrupt();
        
        // wait to die
        try {
            messageThread.join();
        } catch (InterruptedException e) {}
        
        // alert the match maker that the client disconnected
        matchHandle.disconnect();
    }
}

class OutputRunnable implements Runnable {    
    private Socket socket;
    private BlockingQueue<MessageNetClient> queue;

    public OutputRunnable(Socket socket, BlockingQueue<MessageNetClient> queue)
    {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run()
    {
        try {
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            while (true) {
                MessageNetClient message = queue.take();
                
                oos.writeObject(message);
                oos.flush();
            }
        } catch (EOFException e) {
            // connection has ended
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
