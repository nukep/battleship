package battleship.cli;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import battleship.client.NetClient;
import battleship.logic.MessageToClient;

class M2C implements MessageToClient {
    @Override
    public void chat(String message, Date date) {
        System.out.println("Chat: " + date + " : " + message);
    }

    @Override
    public void hitMiss(boolean hit) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void opponentShipSunk() {
        // TODO Auto-generated method stub
        
    }
}

public class Main {
    private static Socket askHost(Scanner k)
    {
        String host;
        
        while (true) {
            System.out.print("Host name: ");
            host = k.nextLine();
            
            try {
                return new Socket(host, 5555);
            } catch (IOException e) {
                // error creating net client
                System.out.println("Error connecting: " + e);
                System.out.println();
            }
        }
    }
    
    public static void main(String[] args)
    {
        M2C m2c = new M2C();
        Scanner k = new Scanner(System.in);
        Socket socket;
        NetClient client;
        String name;
        
        socket = askHost(k);
        /*
        System.out.print("Your name: ");
        name = k.nextLine();
        */
        client = new NetClient(socket, m2c);
        client.start();
    }
}
