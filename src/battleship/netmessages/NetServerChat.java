package battleship.netmessages;

import battleship.logic.MessageToServer;

public class NetServerChat implements MessageNetServer {
    private static final long serialVersionUID = 1L;
    
    private String message;
    
    public NetServerChat(String message)
    {
        this.message = message;
    }
    
    @Override
    public void toServer(MessageToServer s) {
        s.chat(message);
    }
}
