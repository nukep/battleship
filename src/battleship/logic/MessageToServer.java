package battleship.logic;

public interface MessageToServer {
    public void chat(String message);
    public void strikeSquare(int x, int y);
}
