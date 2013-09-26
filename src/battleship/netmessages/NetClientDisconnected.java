package battleship.netmessages;

import battleship.logic.MessageToClient;

public class NetClientDisconnected implements MessageNetClient {

    private boolean opponentLeft;
    
    public NetClientDisconnected(boolean opponentLeft)
    {
        this.opponentLeft = opponentLeft;
    }

    @Override
    public void toClient(MessageToClient c)
    {
        c.disconnected(opponentLeft);
    }

}
