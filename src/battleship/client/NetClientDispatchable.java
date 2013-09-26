package battleship.client;

import battleship.netmessages.MessageNetClient;

public interface NetClientDispatchable {
    public void dispatch(MessageNetClient message);
}
