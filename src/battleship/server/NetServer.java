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

import battleship.logic.MessageToServer;
import battleship.netmessages.NetClientChat;
import battleship.netmessages.MessageNetServer;

class InputRunnable implements Runnable {    
    private Socket socket;
    private MessageToServer m2s;

    public InputRunnable(Socket socket, MessageToServer m2s)
    {
        this.socket = socket;
        this.m2s = m2s;
    }

    @Override
    public void run()
    {
        try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                MessageNetServer message = (MessageNetServer)ois.readObject();
                
                if (!(message instanceof MessageNetServer)) {
                    // object is not the type we were expecting
                    throw new IOException("Message is not a NetServerMessage");
                }
                
                message.toServer(m2s);
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
            
            oos.writeObject(new NetClientChat("Hello!"));
            oos.flush();
        } catch (EOFException e) {
            // connection has ended
        } catch (IOException e) {
        //} catch (InterruptedException e) {
        }
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
