package battleship.netmessages.server;

import battleship.common.MessageToServer;
import battleship.common.Player;
import battleship.netmessages.MessageNetServer;

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
