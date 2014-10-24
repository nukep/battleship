package battleship.netmessages.server;

import battleship.common.MessageToServer;
import battleship.netmessages.MessageNetServer;

public class NetServerDisconnect implements MessageNetServer {
    private static final long serialVersionUID = 1L;

    @Override
    public void toServer(MessageToServer s) {
        s.disconnect();
    }
}
