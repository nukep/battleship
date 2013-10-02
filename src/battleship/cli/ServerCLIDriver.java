package battleship.cli;

import java.io.IOException;
import java.util.ArrayList;

import battleship.common.GameSettings;
import battleship.common.NetConstants;
import battleship.server.NetServer;

public class ServerCLIDriver {
    public static void main(String[] args)
    {
        byte[] shipLengths = {5, 4, 3, 3, 2};
        GameSettings gameSettings = new GameSettings(10, shipLengths);
        
        try {
            NetServer server = new NetServer(gameSettings, NetConstants.DEFAULT_PORT);
            
            System.out.println("Server is started");
            
            new Thread(new ServerCLICommandLine(server), "Server CLI").start();
            
            server.run();
            
            System.out.println("Server is stopped");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e);
        }
    }
}
