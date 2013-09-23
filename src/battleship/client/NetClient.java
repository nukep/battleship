package battleship.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;
import battleship.netmessages.NetServerChat;
import battleship.netmessages.MessageNetClient;
import battleship.netmessages.MessageNetServer;
import battleship.netmessages.NetServerConnect;
import battleship.netmessages.NetServerStrike;

public class NetClient implements MessageToServer {
    private Socket socket;
    private MessageToClient m2c;
    private Thread inputThread, outputThread;
    
    private BlockingQueue<MessageNetServer> outputQueue;
    
    public NetClient(Socket socket, MessageToClient m2c)
    {
        this.socket = socket;
        this.m2c = m2c;
    }
    
    public void start()
    {
        outputQueue = new LinkedBlockingQueue<>();
        
        inputThread = new Thread(new InputRunnable(socket, m2c));
        outputThread = new Thread(new OutputRunnable(socket, outputQueue));
        
        inputThread.start();
        outputThread.start();
    }
    
    public void stop()
    {
        // unblocks any blocking calls in inputThread and outputThread
        inputThread.interrupt();
        outputThread.interrupt();
        
        // end the streams, also unblocking IO reads in inputThread
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (IOException e) {}  // shutdown quietly
    }

    @Override
    public void connect(String name)
    {
        enqueueServerMessage(new NetServerConnect(name));
    }
    
    @Override
    public void chat(final String message)
    {
        enqueueServerMessage(new NetServerChat(message));
    }

    @Override
    public void strikeSquare(int x, int y)
    {
        enqueueServerMessage(new NetServerStrike(x, y));
    }

    private void enqueueServerMessage(MessageNetServer msg)
    {
        outputQueue.add(msg);
    }
}

class InputRunnable implements Runnable {
    private Socket socket;
    private MessageToClient m2c;

    public InputRunnable(Socket socket, MessageToClient m2c)
    {
        this.socket = socket;
        this.m2c = m2c;
    }

    @Override
    public void run()
    {
        try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                MessageNetClient message = (MessageNetClient)ois.readObject();
                
                if (!(message instanceof MessageNetClient)) {
                    // object is not the type we were expecting
                    throw new IOException("Message is not a NetClientMessage");
                }

                message.toClient(m2c);
            }
        } catch (ClassNotFoundException e) {
        } catch (EOFException|SocketException e) {
            // client or server closed the connection
        } catch (IOException e) {
            System.err.println("Client IO error (read): " + e);
        }
        
        System.out.println("Input thread ended");
        
        try {
            socket.close();
        } catch (IOException e) {}
    }
}

class OutputRunnable implements Runnable {
    private Socket socket;
    private BlockingQueue<MessageNetServer> queue;
    
    public OutputRunnable(Socket socket, BlockingQueue<MessageNetServer> queue)
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
                MessageNetServer message = queue.take();
                
                oos.writeObject(message);
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println("Client IO error (write): " + e);
        } catch (InterruptedException e) {
            // client is trying to shut down
        }
    }
}
