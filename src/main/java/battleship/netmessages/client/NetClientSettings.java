package battleship.netmessages.client;

import battleship.common.GameSettings;
import battleship.common.MessageToClient;
import battleship.netmessages.MessageNetClient;

public class NetClientSettings implements MessageNetClient {
    private static final long serialVersionUID = 1L;
    
    private int boardSize;
    private byte[] shipLengths;

    public NetClientSettings(GameSettings settings)
    {
        this.boardSize = settings.getBoardSize();
        this.shipLengths = settings.getShipLengths();
    }

    @Override
    public void toClient(MessageToClient c)
    {
        c.settings(new GameSettings(boardSize, shipLengths));
    }
}
