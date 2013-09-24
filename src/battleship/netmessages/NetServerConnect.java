package battleship.netmessages;

import battleship.logic.MessageToServer;
import battleship.logic.Player;

public class NetServerConnect implements MessageNetServer {
    private static final long serialVersionUID = 1L;
    
    private String name;
    
    public NetServerConnect(String name)
    {
        this.name = name;
    }
    
    public Player toPlayer()
    {
        return new Player(name);
    }
    
    @Override
    public void toServer(MessageToServer s) {
        s.connect(name);
    }
}
