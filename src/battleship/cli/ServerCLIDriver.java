package battleship.cli;

import java.io.IOException;

import battleship.logic.NetConstants;
import battleship.server.NetServer;

public class ServerCLIDriver {
    public static void main(String[] args)
    {
        try {
            NetServer server = new NetServer(NetConstants.DEFAULT_PORT);
            
            System.out.println("Server is started");
            
            new Thread(new ServerCLICommandLine(server), "Server CLI").start();
            
            server.run();
            
            System.out.println("Server is stopped");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e);
        }
    }
}
