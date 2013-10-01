package battleship.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import battleship.common.MessageToServer;
import battleship.common.ShipConfiguration;
import battleship.netmessages.*;
import battleship.netmessages.client.NetClientDisconnected;
import battleship.netmessages.server.*;

/**
 * The NetClient class is responsible for sending and receiving messages from
 * a server connection.
 *
 */
public class NetClient implements MessageToServer {
    /**
     * The NetClient Dispatcher interface allows an implementation to
     * dispatch a MessageNetClient in any way it sees fit.
     * <p>
     * For example, the Swing GUI uses SwingUtilities.invokeLater() to ensure
     * that all messages are called on the Swing thread.
     * </p>
     */
    public interface Dispatcher {
        public void dispatch(MessageNetClient message);
    }
    
    private Socket socket;
    private Dispatcher dispatcher;
    private Thread inputThread, outputThread;
    
    private BlockingQueue<MessageNetServer> outputQueue;
    
    public NetClient(Socket socket, Dispatcher dispatcher)
    {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }
    
    public void start()
    {
        outputQueue = new LinkedBlockingQueue<>();
        
        inputThread = new Thread(new InputRunnable(socket, dispatcher));
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
        outputQueue.add(new NetServerConnect(name));
    }
    
    @Override
    public void chat(final String message)
    {
        outputQueue.add(new NetServerChat(message));
    }

    @Override
    public void configureFleet(ShipConfiguration shipConfiguration)
    {
        outputQueue.add(new NetServerConfigureFleet(shipConfiguration));
    }

    @Override
    public void strikeSquare(int x, int y)
    {
        outputQueue.add(new NetServerStrike(x, y));
    }
}

class InputRunnable implements Runnable {
    private Socket socket;
    private NetClient.Dispatcher dispatcher;

    public InputRunnable(Socket socket, NetClient.Dispatcher dispatcher)
    {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run()
    {
        try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                MessageNetClient message = (MessageNetClient)ois.readObject();
                dispatcher.dispatch(message);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException|SocketException e) {
            // client or server closed the connection
        } catch (IOException e) {
            System.err.println("Client IO error (read): " + e);
        }
        
        try {
            socket.close();
        } catch (IOException e) {}
        
        // we've been disconnected - disconnected message
        dispatcher.dispatch(new NetClientDisconnected(false));
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
            Thread.currentThread().interrupt();
        }
    }
}
