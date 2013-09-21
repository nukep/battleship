package battleship.netmessages;

import java.util.Date;

import battleship.logic.MessageToClient;

public class NetClientChat implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private String message;
    private Date date;
    
    public NetClientChat(String message)
    {
        this.message = message;
        this.date = new Date();
    }

    @Override
    public void toClient(MessageToClient c) {
        c.chat(message, date);
    }
}
