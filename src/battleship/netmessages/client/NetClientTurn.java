package battleship.netmessages.client;

import battleship.common.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientTurn implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private boolean yourTurn;
    
    public NetClientTurn(boolean yourTurn)
    {
        this.yourTurn = yourTurn;
    }

    @Override
    public void toClient(MessageToClient c)
    {
        c.firstTurn(yourTurn);
    }
}
