package battleship.netmessages.client;

import battleship.logic.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientHitMiss implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private boolean hit;

    public NetClientHitMiss(boolean hit)
    {
        this.hit = hit;
    }
    
    @Override
    public void toClient(MessageToClient c) {
        c.hitMiss(hit);
    }

}
