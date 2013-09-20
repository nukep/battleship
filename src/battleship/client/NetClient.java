package battleship.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;

class InputRunnable implements Runnable {
    private InputStream is;
    private MessageToClient m2c;

    public InputRunnable(InputStream is, MessageToClient m2c) {
        this.is = is;
        this.m2c = m2c;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            
            while (true) {
                String message;
                Date date;
                message = ois.readUTF();
                date = (Date)ois.readObject();
                
                m2c.chat(message, date);
            }
        } catch (ClassNotFoundException e) {
        } catch (EOFException|SocketException e) {
            // client or server closed the connection
        } catch (IOException e) {
            System.err.println("Client IO error: " + e);
        }
        
        System.out.println("Input thread ended");
    }
}

public class NetClient implements MessageToServer {
    private Socket socket;
    private MessageToClient m2c;
    private Thread inputThread;
    
    public NetClient(Socket socket, MessageToClient m2c)
    {
        this.socket = socket;
        this.m2c = m2c;
    }
    
    public void start()
    {
        try {
            inputThread = new Thread(new InputRunnable(socket.getInputStream(), m2c));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        inputThread.start();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        // unblocks any blocking calls in inputThread
        inputThread.interrupt();
        
        // end the streams, also unblocking IO reads in inputThread
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (IOException e) {}  // close quietly
    }

    @Override
    public void chat(String message) {
        
    }

    @Override
    public void strikeSquare(int x, int y) {
        // TODO Auto-generated method stub
        
    }
}
