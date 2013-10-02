package battleship.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import battleship.common.GameSettings;

/**
 * The NetServer class is responsible for accepting and maintaining client
 * connections and their games.
 *
 */
public class NetServer implements Runnable {
    private ServerSocket server_socket;
    private NetMatchmaker matchMaker;

    public NetServer(GameSettings gameSettings, int port, NetServerNotify serverNotify) throws IOException
    {
        server_socket = new ServerSocket(port);
        matchMaker = new NetMatchmaker(gameSettings, serverNotify);
    }

    @Override
    public void run()
    {
        boolean running = true;
        
        while (running) {
            try {
                Socket s = server_socket.accept();
                
                NetConnection c = new NetConnection(s, matchMaker);
                c.start();
            } catch (SocketException e) {
                // server is trying to stop
                running = false;
            } catch (IOException e) {
                System.err.println("Server IO error: " + e);
            }
        }
        
        // close all ongoing games
        matchMaker.closeAll();
    }
    
    public void stop()
    {
        try {
            server_socket.close();
        } catch (IOException e) {}  // close silently
    }
}
