package battleship.gui;

import java.io.IOException;
import java.net.Socket;

import battleship.client.NetClient;
import battleship.client.NetClientDispatchToSwing;
import battleship.client.NetClientDispatchable;
import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;
import battleship.logic.NetConstants;

public class Connect implements Runnable {
    private String name;
    private String host;
    private ConnectListener connectListener;
    private NetClientDispatchable dispatcher;
    
    private Socket socket;
    private NetClient client;

    public Connect(String name, String host, ConnectListener connectListener,
                   MessageToClient m2c)
    {
        this.name = name;
        this.host = host;
        this.connectListener = connectListener;
        this.dispatcher = new NetClientDispatchToSwing(m2c);
        
        this.client = null;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, NetConstants.DEFAULT_PORT);
            
            client = new NetClient(socket, dispatcher);
            client.start();
            client.connect(name);
            
            connectListener.connect(this);
        } catch (IOException e) {
            connectListener.error(e);
        }
    }
    
    public MessageToServer getMessageToServer()
    {
        return client;
    }
}

interface ConnectListener {
    public void connect(Connect c);
    public void error(Exception e);
}
