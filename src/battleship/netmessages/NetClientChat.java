package battleship.netmessages;

import java.util.Date;

import battleship.logic.MessageToClient;

public class NetClientChat implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private String message;
    private Date date;
    
    public NetClientChat(String message, Date date)
    {
        this.message = message;
        this.date = date;
    }

    @Override
    public void toClient(MessageToClient c) {
        c.chat(message, date);
    }
}