package battleship.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;

class ServerAcceptRunnable implements Runnable {
    private ServerSocket svr;
    private boolean serverRunning;
    /* even though there should only be one pending player at most,
     * a queue represents the problem better
     */
    private Queue<NetworkPlayer> pendingPlayers;

    public ServerAcceptRunnable(ServerSocket socket)
    {
        this.svr = socket;
        this.pendingPlayers = new LinkedList<>();
        this.serverRunning = true;
    }
    
    private synchronized boolean isRunning()
    {
        return serverRunning;
    }
    
    public synchronized void stop()
    {
        serverRunning = false;
    }
    
    private void pairPlayer(NetworkPlayer p)
    {
        if (pendingPlayers.isEmpty()) {
            pendingPlayers.add(p);
        } else {
            NetworkPlayer opponent = pendingPlayers.remove();
        }
    }
    
    private void acceptConnection() throws IOException
    {
        Socket socket = svr.accept();
    }
    
    @Override
    public void run()
    {
        while (isRunning()) {
            try {
                acceptConnection();
            } catch (SocketTimeoutException e) {
                // socket timeouts are expected, so ignore...
            } catch (IOException e) {
                // other problem accepting the connection
                e.printStackTrace();
            }
        }
        
        // no longer accepting - quit all current games
        // TODO
        
        // close server socket
        try {
            svr.close();
        } catch (IOException e) {
        }
        
        // end of thread
    }
}

public class ServerListener {
    private Thread thread;
    private int port;
    private ServerAcceptRunnable serverAcceptor;
    
    private static final int ACCEPT_TIMEOUT = 1000;
    
    public ServerListener(int port)
    {
        this.port = port;
        thread = null;
    }
    
    /**
     * Stop listening for connections, and quit all ongoing games.
     */
    public void stop()
    {
        serverAcceptor.stop();
    }
    
    /**
     * The same as stop(), but blocks the current thread until the server is
     * fully stopped.
     */
    public void stopAndWait()
    {
        stop();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void start() throws IOException
    {
        ServerSocket svr = new ServerSocket(port);
        svr.setSoTimeout(ACCEPT_TIMEOUT);
        
        serverAcceptor = new ServerAcceptRunnable(svr);
        
        // Initialize the listening thread
        thread = new Thread(serverAcceptor);
        
        thread.start();
    }
}