package battleship.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;

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
                    NetConnection p1, p2;
                    p1 = players.poll();
                    p2 = players.poll();
                    
                    new NetGame(p1, p2);
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
