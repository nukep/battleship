package battleship.netmessages;

import battleship.logic.MessageToClient;

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
        c.turn(yourTurn);
    }
}
