package battleship.common;

/**
 * Client -> Server
 * <p>
 * The MessageToServer interface is <i>very</i> important.
 * It is used on both the server and client side for message passing.
 * <p>
 * The server uses MessageToServer to receive messages from the client.<br>
 * The client uses MessageToServer to send messages to the server.
 * 
 * @see MessageToClient
 * @see battleship.netmessages.MessageNetServer
 */
public interface MessageToServer {
    /**
     * The first message, confirming connection.
     * 
     * @param name Player name
     */
    public void connect(String name);
    /**
     * Send a chat message
     * 
     * @param message
     */
    public void chat(String message);
    /**
     * The client is done configuring their fleet.
     * 
     * @param shipConfiguration
     */
    public void configureFleet(ShipConfiguration shipConfiguration);
    /**
     * Strike one of the opponent's squares. It's now the opponent's turn.
     * 
     * @param x
     * @param y
     */
    public void strikeSquare(int x, int y);
    /**
     * Tell the server to gracefully disconnect the client.
     */
    public void disconnect();
}
