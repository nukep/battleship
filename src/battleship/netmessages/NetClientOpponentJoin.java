package battleship.netmessages;

import battleship.logic.MessageToClient;

public class NetClientOpponentJoin implements MessageNetClient {
    private static final long serialVersionUID = 1L;

    private String name;
    private boolean yourTurnFirst;

    public NetClientOpponentJoin(String name, boolean yourTurnFirst)
    {
        this.name = name;
        this.yourTurnFirst = yourTurnFirst;
    }

    @Override
    public void toClient(MessageToClient c)
    {
        c.opponentJoin(name, yourTurnFirst);
    }
}
