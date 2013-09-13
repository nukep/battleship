package battleship.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MessageLayer {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    public MessageLayer(InputStream is, OutputStream os) throws IOException
    {
        ois = new ObjectInputStream(is);
        oos = new ObjectOutputStream(oos);
    }
    
    public Message readMessage() throws IOException
    {
        Message m = new Message();
        
        return m;
    }
    
    public void sendMessage(Message m) throws IOException
    {
        
    }
}
