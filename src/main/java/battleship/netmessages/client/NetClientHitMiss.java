package battleship.netmessages.client;

import battleship.common.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientHitMiss implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private boolean hit, shipSunk;

    public NetClientHitMiss(boolean hit, boolean shipSunk)
    {
        this.hit = hit;
        this.shipSunk = shipSunk;
    }
    
    @Override
    public void toClient(MessageToClient c) {
        c.hitMiss(hit, shipSunk);
    }

}
