package battleship.logic;

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
    public void configureFleet(ShipConfiguration shipConfiguration);
    /**
     * Strike one of the opponent's squares. It's now the opponent's turn.
     * 
     * @param x
     * @param y
     */
    public void strikeSquare(int x, int y);
    /**
     * Send the server your fleet configuration.
     */
    //public void submitFleet();
}
