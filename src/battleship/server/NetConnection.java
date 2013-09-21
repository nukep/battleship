package battleship.server;

import java.io.IOException;
import java.net.Socket;

import battleship.logic.MessageToServer;

class NetConnection {
    private Socket socket;
    private Thread inThread, outThread;

    public NetConnection(Socket socket)
    {
        this.socket = socket;
    }
    
    public void start()
    {
        MessageToServer m2s = new MessageToServer() {
            
            @Override
            public void strikeSquare(int x, int y) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void chat(String message) {
                // TODO Auto-generated method stub
                
            }
        };
        inThread = new Thread(new InputRunnable(socket, m2s));
        outThread = new Thread(new OutputRunnable(socket));
        
        inThread.start();
        outThread.start();
    }
    
    public void stop()
    {
        // unblock any IO read/write methods
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (IOException e) {}  // silently close
        
        // unblock any thread-blocking methods (e.g. wait, sleep, join)
        inThread.interrupt();
        outThread.interrupt();
    }
    
    public void stopAndWait() throws InterruptedException
    {
        stop();
        inThread.join();
        outThread.join();
    }
}