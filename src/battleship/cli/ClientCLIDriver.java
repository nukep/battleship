package battleship.cli;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

import battleship.client.NetClient;
import battleship.logic.MessageToClient;
import battleship.logic.Player;

class M2C implements MessageToClient {
    private String opponentName;
    
    @Override
    public void opponentJoin(String name) {
        this.opponentName = name;
        System.out.println(opponentName + " joined!");
    }

    @Override
    public void opponentLeave() {
        System.out.println(opponentName + " left!");
    }

    @Override
    public void turn(boolean yourTurn) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void chat(String message, Date date) {
        System.out.println(date + " : " + message);
    }

    @Override
    public void hitMiss(boolean hit) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void opponentStrike(int x, int y) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameComplete(boolean youWin) {
        // TODO Auto-generated method stub
        
    }
}

public class ClientCLIDriver {
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
        MessageToClient m2c = new M2C();
        Scanner k = new Scanner(System.in);
        Socket socket;
        NetClient client;
        String name;
        
        socket = askHost(k);
        
        System.out.print("Your name: ");
        name = k.nextLine();
        
        client = new NetClient(socket, m2c);
        client.start();
        
        client.connect(name);
        
        while (true) {
            String msg = k.nextLine();
            client.chat(msg);
        }
    }
}
