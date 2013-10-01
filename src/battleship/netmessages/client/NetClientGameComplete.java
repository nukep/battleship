package battleship.netmessages.client;

import battleship.logic.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientGameComplete implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private boolean youWin;
    
    public NetClientGameComplete(boolean youWin)
    {
        this.youWin = youWin;
    }

    @Override
    public void toClient(MessageToClient c)
    {
        c.gameComplete(youWin);
    }
}
