package battleship.cli;

import java.util.Scanner;

import battleship.server.NetServer;

public class ServerCLICommandLine implements Runnable {
    private NetServer server;
    
    public ServerCLICommandLine(NetServer server)
    {
        this.server = server;
    }
    
    @Override
    public void run()
    {
        Scanner k = new Scanner(System.in);
        
        boolean isRunning = true;
        
        while (isRunning) {
            System.out.print("> ");
            String line = k.nextLine();
            
            if (line.equals("stop")) {
                server.stop();
                isRunning = false;
            } else {
                System.out.println("Invalid command");
            }
        }
    }
}
