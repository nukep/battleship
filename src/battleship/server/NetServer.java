package battleship.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

class InputRunnable implements Runnable {    
    private Socket socket;

    public InputRunnable(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (EOFException e) {
            // connection has ended
        } catch (IOException e) {
        }
        
        System.out.println("Client disconnected");
    }
}

class OutputRunnable implements Runnable {    
    private Socket socket;

    public OutputRunnable(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try {
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(socket.getOutputStream());
            
            oos.writeUTF("Hello!");
            oos.writeObject(new java.util.Date());
        } catch (EOFException e) {
            // connection has ended
        } catch (IOException e) {
        }
    }
}

class NetConnection {
    private Socket socket;
    private Thread inThread, outThread;

    public NetConnection(Socket socket)
    {
        this.socket = socket;
    }
    
    public void start()
    {
        inThread = new Thread(new InputRunnable(socket));
        outThread = new Thread(new OutputRunnable(socket));
        
        inThread.start();
        outThread.start();
    }
    
    public void stop()
    {
        // unblock any IO read/write methods
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
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
}

public class NetServer implements Runnable {
    private ServerSocket server_socket;

    public NetServer(int port) throws IOException
    {
        server_socket = new ServerSocket(port);
    }

    @Override
    public void run()
    {
        boolean running = true;
        
        
        Queue<NetConnection> players = new LinkedList<>();
        
        while (running) {
            try {
                Socket s = server_socket.accept();
                
                NetConnection c = new NetConnection(s);
                players.add(c);
                c.start();
                
                while (players.size() >= 2) {
                    players.poll();
                    players.poll();
                }
            } catch (SocketException e) {
                // server is trying to stop
                running = false;
            } catch (IOException e) {
                System.err.println("Server IO error: " + e);
            }
        }
        
        // close all connections
        for (NetConnection p: players) {
            p.stop();
        }
    }
    
    public void stop()
    {
        try {
            server_socket.close();
        } catch (IOException e) {}  // close silently
    }
}
