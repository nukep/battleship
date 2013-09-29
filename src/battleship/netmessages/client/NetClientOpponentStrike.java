package battleship.netmessages.client;

import battleship.logic.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientOpponentStrike implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private int x;
    private int y;
    
    public NetClientOpponentStrike(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void toClient(MessageToClient c) {
        c.opponentStrike(x, y);
    }
}
