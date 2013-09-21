package battleship.netmessages;

import java.io.Serializable;
import battleship.logic.MessageToServer;

/**
 * Client -> Server
 * A MessageNetServer is serialized over the network to the server
 *
 * The toServer() method is an example of the visitor pattern.
 * It avoids having to use "instanceof" on different message types.
 */
public interface MessageNetServer extends Serializable {
    public void toServer(MessageToServer s);
}
