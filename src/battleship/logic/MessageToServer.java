package battleship.logic;

public interface MessageToServer {
    /**
     * Client sends a chat message
     * @param message
     */
    public void chat(String message);
    public void strikeSquare(int x, int y);
}
