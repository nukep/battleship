package battleship.clientgui;

import java.io.IOException;
import java.net.Socket;

import battleship.client.NetClient;
import battleship.client.NetClientDispatcher;
import battleship.common.MessageToServer;
import battleship.common.NetConstants;

public class Connect implements Runnable {
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
    
    public void start(NetClientDispatcher dispatcher)
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

interface ConnectListener {
    public void connect(Connect c);
    public void error(Exception e);
}
