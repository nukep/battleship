package battleship.netmessages;

import battleship.logic.MessageToServer;

public class NetServerConnect implements MessageNetServer {
    private static final long serialVersionUID = 1L;
    
    private String name;
    
    public NetServerConnect(String name)
    {
        this.name = name;
    }
    
    @Override
    public void toServer(MessageToServer s) {
        s.connect(name);
    }
}
