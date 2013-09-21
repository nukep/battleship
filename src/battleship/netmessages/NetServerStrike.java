package battleship.netmessages;

import battleship.logic.MessageToServer;

public class NetServerStrike implements MessageNetServer {
    private static final long serialVersionUID = 1L;
    
    int x, y;

    public NetServerStrike(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public void toServer(MessageToServer s)
    {
        s.strikeSquare(x, y);
    }
}
