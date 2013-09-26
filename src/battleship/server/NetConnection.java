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

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;
import battleship.logic.Player;
import battleship.netmessages.MessageNetClient;
import battleship.netmessages.MessageNetServer;
import battleship.netmessages.NetClientChat;
import battleship.netmessages.NetClientDisconnected;
import battleship.netmessages.NetClientOpponentJoin;
import battleship.netmessages.NetServerConnect;

class NetConnection implements MessageToClient {
    private Socket socket;
    private Thread inThread, outThread;
    private InputRunnable inputRunnable;
    private BlockingQueue<MessageNetClient> outputQueue;
    private NetMatchHandle matchHandle;

    public NetConnection(Socket socket, NetMatchmaker matchmaker)
    {
        this.socket = socket;
        this.matchHandle = new NetMatchHandle(matchmaker, this);
    }
    
    public void start()
    {
        outputQueue = new LinkedBlockingQueue<>();

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
        inThread.interrupt();
        outThread.interrupt();
    }
    
    public void stopAndWait() throws InterruptedException
    {
        close();
        inThread.join();
        outThread.join();
    }

    @Override
    public void opponentJoin(String name) {
        enqueueClientMessage(new NetClientOpponentJoin(name));
    }

    @Override
    public void disconnected(boolean opponentLeft) {
        enqueueClientMessage(new NetClientDisconnected(opponentLeft));
    }

    @Override
    public void turn(boolean yourTurn) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chat(String message, Date date) {
        enqueueClientMessage(new NetClientChat(message, date));
    }

    @Override
    public void hitMiss(boolean hit) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void opponentStrike(int x, int y) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameComplete(boolean youWin) {
        // TODO Auto-generated method stub
        
    }

    private void enqueueClientMessage(MessageNetClient msg)
    {
        outputQueue.add(msg);
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
    class InputMessageRunnable implements Runnable {
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            
        }
    } 
    private Socket socket;
    private NetMatchHandle matchHandle;

    public InputRunnable(Socket socket, NetMatchHandle matchHandle)
    {
        this.socket = socket;
        this.matchHandle = matchHandle;
    }

    @Override
    public void run()
    {
        try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            
            NetServerConnect connectMessage;
            connectMessage = (NetServerConnect)ois.readObject();
            
            if (!(connectMessage instanceof NetServerConnect)) {
                throw new IOException("Message is not NetServerConnect");
            }
            
            NetGamePlayerHandle playerHandle;
            
            // register connection with the server (matches players)
            // wait for a match
            // TODO - non-socket blocking is forbidden in Input thread
            playerHandle = matchHandle.connect(connectMessage.toPlayer());
            
            playerHandle.messageToServer(connectMessage);
            
            while (true) {
                MessageNetServer message;
                message = (MessageNetServer)ois.readObject();
                
                if (!(message instanceof MessageNetServer)) {
                    // object is not the type we were expecting
                    throw new IOException("Message is not a NetServerMessage");
                }

                playerHandle.messageToServer(message);
            }
        } catch (EOFException|SocketException e) {
            // connection has ended
        } catch (IOException e) {
            System.err.println("Server IO error (" + socket.getInetAddress() + "): " + e);
        } catch (ClassNotFoundException e) {
            // this is a bad thing to happen
            e.printStackTrace();
        }
        
        // close the socket
        try {
            socket.close();
        } catch (IOException e) {}
        
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
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }
}
