package battleship.netmessages;

import battleship.logic.MessageToClient;

public class NetClientOpponentJoin implements MessageNetClient {
    private static final long serialVersionUID = 1L;

    private String name;

    public NetClientOpponentJoin(String name)
    {
        this.name = name;
    }

    @Override
    public void toClient(MessageToClient c)
    {
        c.opponentJoin(name);
    }
}
