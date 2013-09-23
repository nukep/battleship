package battleship.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;
import battleship.netmessages.MessageNetClient;
import battleship.netmessages.MessageNetServer;
import battleship.netmessages.NetClientChat;
import battleship.netmessages.NetClientOpponentJoin;
import battleship.netmessages.NetServerConnect;

class NetConnection implements MessageToClient {
    private Socket socket;
    private Thread inThread, outThread;
    private InputRunnable inputRunnable;
    private BlockingQueue<MessageNetClient> outputQueue;

    public NetConnection(Socket socket)
    {
        this.socket = socket;
    }
    
    public void start()
    {
        outputQueue = new LinkedBlockingQueue<>();

        inputRunnable = new InputRunnable(socket);
        inThread = new Thread(inputRunnable);
        outThread = new Thread(new OutputRunnable(socket, outputQueue));
        
        inThread.start();
        outThread.start();
    }
    
    public void stop()
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
        stop();
        inThread.join();
        outThread.join();
    }
    
    public AssignMessageToServer getAssignMessageToServer()
    {
        return inputRunnable;
    }

    @Override
    public void opponentJoin(String name) {
        enqueueClientMessage(new NetClientOpponentJoin(name));
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

class InputRunnable implements Runnable, AssignMessageToServer {    
    private Socket socket;
    private MessageToServer m2s;

    public InputRunnable(Socket socket)
    {
        this.socket = socket;
        this.m2s = null;
    }
    
    @Override
    public synchronized void assignMessageToServer(MessageToServer m2s)
    {
        this.m2s = m2s;
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
            
            while (true) {
                MessageNetServer message;
                message = (MessageNetServer)ois.readObject();
                
                if (!(message instanceof MessageNetServer)) {
                    // object is not the type we were expecting
                    throw new IOException("Message is not a NetServerMessage");
                }
                
                synchronized (this) {
                    if (m2s == null) {
                        System.err.println("Server IO error: Client sent message prematurely");
                    } else {
                        message.toServer(m2s);
                    }
                }
            }
        } catch (EOFException e) {
            // connection has ended
        } catch (IOException e) {
            System.err.println("Server IO error (" + socket.getInetAddress() + "): " + e);
        } catch (ClassNotFoundException e) {
            // this is a bad thing to happen
            e.printStackTrace();
        }
        
        System.out.println("Client disconnected");
        
        try {
            socket.close();
        } catch (IOException e) {}
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