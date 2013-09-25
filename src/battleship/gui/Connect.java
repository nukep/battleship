package battleship.gui;

import java.io.IOException;
import java.net.Socket;

import battleship.client.NetClient;
import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;
import battleship.logic.NetConstants;

public class Connect implements Runnable {
    private String name;
    private String host;
    private ConnectInterface connectInterface;
    private MessageToClient m2c;
    
    private Socket socket;
    private NetClient client;

    public Connect(String name, String host, ConnectInterface connectInterface,
                   MessageToClient m2c)
    {
        this.name = name;
        this.host = host;
        this.connectInterface = connectInterface;
        this.m2c = m2c;
        
        this.client = null;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, NetConstants.DEFAULT_PORT);
            
            client = new NetClient(socket, m2c);
            client.start();
            client.connect(name);
            
            connectInterface.connect(this);
        } catch (IOException e) {
            connectInterface.error(e);
        }
    }
    
    public MessageToServer getMessageToServer()
    {
        return client;
    }
}

interface ConnectInterface {
    public void connect(Connect c);
    public void error(Exception e);
}