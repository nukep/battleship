package battleship.logic;

public interface MessageToServer {
    /**
     * Send a chat message
     * 
     * @param message
     */
    public void chat(String message);
    /**
     * Strike one of the opponent's squares. It's now the opponent's turn.
     * 
     * @param x
     * @param y
     */
    public void strikeSquare(int x, int y);
}
