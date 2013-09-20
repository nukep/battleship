package battleship.cli;

import java.io.IOException;

import battleship.server.NetServer;

public class ServerCLIDriver {
    public static void main(String[] args)
    {
        try {
            NetServer server = new NetServer(5555);
            
            System.out.println("Server is started");
            
            server.run();
            
            System.out.println("Server is stopped");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e);
        }
    }
}
