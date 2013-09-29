package battleship.client;

import battleship.netmessages.MessageNetClient;

public interface NetClientDispatcher {
    public void dispatch(MessageNetClient message);
}
