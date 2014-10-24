package battleship.netmessages.client;

import battleship.common.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientDisconnected implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
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
