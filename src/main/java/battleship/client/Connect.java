package battleship.client;

import java.io.IOException;
import java.net.Socket;

import battleship.common.MessageToServer;
import battleship.common.NetConstants;

/**
 * The Connect class attempts to connect to a server host.
 * <p>
 * When connection succeeds or fails, the Connect.ConnectionListener interface
 * methods are called.
 * </p>
 */
public class Connect implements Runnable {
    public interface ConnectListener {
        public void connect(Connect c);
        public void error(Exception e);
    }

    private String name;
    private String host;
    private ConnectListener connectListener;
    
    private Socket socket;
    private NetClient client;

    public Connect(String name, String host, ConnectListener connectListener)
    {
        this.name = name;
        this.host = host;
        this.connectListener = connectListener;
        
        this.client = null;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, NetConstants.DEFAULT_PORT);
            connectListener.connect(this);
        } catch (IOException e) {
            connectListener.error(e);
        }
    }
    
    public void start(NetClient.Dispatcher dispatcher)
    {
        client = new NetClient(socket, dispatcher);
        client.start();
        client.connect(name);
    }
    
    public MessageToServer getMessageToServer()
    {
        return client;
    }

    public String getPlayerName()
    {
        return name;
    }
}

