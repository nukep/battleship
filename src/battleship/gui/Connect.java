package battleship.gui;

import java.io.IOException;
import java.net.Socket;

import battleship.client.NetClient;
import battleship.logic.MessageToClient;

public class Connect implements Runnable {
    private String name;
    private String host;
    private BusyInterface busy;
    private MessageToClient m2c;
    
    private NetClient client;
    private Exception connectException;

    public Connect(String name, String host, BusyInterface busy,
                   MessageToClient m2c)
    {
        this.name = name;
        this.host = host;
        this.busy = busy;
        this.m2c = m2c;
        
        this.client = null;
        this.connectException = null;
    }

    @Override
    public void run() {
        busy.busy();
        
        try {
            Socket socket = new Socket(host, 5555);
            
            client = new NetClient(socket, m2c);
            client.connect(name);
        } catch (IOException e) {
            connectException = e;
        }
        
        busy.unbusy();
    }
    
    public Exception getException()
    {
        return connectException;
    }
    
    public NetClient getNetClient()
    {
        return client;
    }
}
