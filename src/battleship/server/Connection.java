package battleship.server;

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;

public interface Connection {
    public String getName();
    public MessageToClient getMessageToClient();
    public void assignMessagetoServer(MessageToServer m2s);
}
