package battleship.netmessages;

import java.io.Serializable;

import battleship.common.MessageToClient;

/**
 * Server -> Client
 * <p>
 * A MessageNetClient is serialized over the network to the client
 * <p>
 * The toClient() method is an example of the visitor pattern.
 * It avoids having to use <code>instanceof</code> on different message types.
 * 
 * @see MessageNetServer
 */
public interface MessageNetClient extends Serializable {
    public void toClient(MessageToClient c);
}
