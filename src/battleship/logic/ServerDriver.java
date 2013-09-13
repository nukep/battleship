package battleship.logic;

import java.io.IOException;

public class ServerDriver {

    /**
     * @param args
     * @throws IOException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws IOException, InterruptedException
    {
        ServerListener listener = new ServerListener(1234);
        
        listener.start();
        
        Thread.sleep(10 * 1000);
        
        listener.stopAndWait();
    }

}
