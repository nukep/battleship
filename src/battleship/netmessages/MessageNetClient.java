package battleship.netmessages;

import java.io.Serializable;
import battleship.logic.MessageToClient;

/**
 * Server -> Client
 * A MessageNetClient is serialized over the network to the client
 *
 * The toClient() method is an example of the visitor pattern.
 * It avoids having to use "instanceof" on different message types
 */
public interface MessageNetClient extends Serializable {
    public void toClient(MessageToClient c);
}
