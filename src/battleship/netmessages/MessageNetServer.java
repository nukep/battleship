package battleship.netmessages;

import java.io.Serializable;

import battleship.common.MessageToServer;

/**
 * Client -> Server
 * <p>
 * A MessageNetServer is serialized over the network to the server
 * <p>
 * The toServer() method is an example of the visitor pattern.
 * It avoids having to use <code>instanceof</code> on different message types.
 * 
 * @see MessageNetClient
 */
public interface MessageNetServer extends Serializable {
    public void toServer(MessageToServer s);
}
