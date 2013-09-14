package battleship.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class GameConnectionRunnable implements Runnable
{
    private GameConnection connection;
    Socket s;
    
    public GameConnectionRunnable(GameConnection connection)
    {
        this.connection = connection;
    }
    
    @Override
    public void run()
    {
        try {
            s.getInputStream().read();
        } catch (SocketException e) {
        } catch (IOException e) {
        }
    }
}

class GameConnection {
    private Socket socket;
    private Thread thread;
    private MessageLayer msg;
    
    public GameConnection(Socket socket) throws IOException
    {
        this.socket = socket;
        this.msg = new MessageLayer(socket.getInputStream(),
                                    socket.getOutputStream());
        
        // each connection gets its own thread
        thread = new Thread(new GameConnectionRunnable(this));
    }
    
    public void start()
    {
        thread.start();
    }
    
    public void quit()
    {
        // close the socket, therefore unblocking any ongoing read/writes
        try {
            socket.close();
        } catch (IOException e) {
        }
        
        // wait for the game connection thread to die
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        try {
            socket.close();
        } catch (IOException e) {
            // closing the socket failed somehow. nothing else can be done.
        }
    }
}

class ServerAcceptRunnable implements Runnable {
    private ServerSocket svr;
    private boolean serverRunning;
    private List<GameConnection> gameConnections;
    /* even though there should only be one pending player at most,
     * a queue represents the problem better
     */
    private Queue<NetworkPlayer> pendingPlayers;

    public ServerAcceptRunnable(ServerSocket socket,
                                List<GameConnection> gameConnections)
    {
        this.svr = socket;
        this.gameConnections = gameConnections;
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
    
    private void acceptConnection() throws IOException, SocketTimeoutException
    {
        Socket socket = svr.accept();
        
        MessageLayer ml = new MessageLayer(socket.getInputStream(),
                                           socket.getOutputStream());
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
        for (GameConnection g: gameConnections) {
            g.quit();
        }
        // close server socket
        try {
            svr.close();
        } catch (IOException e) {
        }
        
        // end of thread
    }
}

public class ServerListener {
    private LinkedList<GameConnection> gameConnections;
    private Thread thread;
    private int port;
    
    private static final int ACCEPT_TIMEOUT = 1000;
    
    public ServerListener(int port)
    {
        this.port = port;
        gameConnections = new LinkedList<>();
        thread = null;
    }
    
    /**
     * Stop listening for connections, and quit all ongoing games.
     */
    public void stop()
    {
        
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
        
        // Initialize the listening thread
        thread = new Thread(new ServerAcceptRunnable(svr, gameConnections));
        
        thread.start();
    }
}