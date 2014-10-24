package battleship.common;

/**
 * The GameSettings class stores all customizable settings for a game.
 * <p>
 * In this case, it stores the board size (size x size), and an array of
 * placeable ship lengths.
 *
 */
public class GameSettings {
    private int boardSize;
    private byte[] shipLengths;
    
    public GameSettings(int boardSize, byte[] shipLengths)
    {
        this.boardSize = boardSize;
        this.shipLengths = shipLengths;
    }

    public int getBoardSize()
    {
        return boardSize;
    }

    public void setBoardSize(int boardSize)
    {
        this.boardSize = boardSize;
    }

    public byte[] getShipLengths()
    {
        return shipLengths;
    }

    public void setShipLengths(byte[] shipLengths)
    {
        this.shipLengths = shipLengths;
    }
}
