package battleship.common;

/**
 * The Ship class describes a ship's top-left coordinates, its length, and
 * whether it's laid vertical or horizontal.
 *
 */
public class Ship {
    private int x, y;
    private int length;
    private boolean isHorizontal;
    
    public Ship(int x, int y, int length, boolean isHorizontal)
    {
        this.x = x;
        this.y = y;
        this.length = length;
        this.isHorizontal = isHorizontal;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getLength()
    {
        return length;
    }

    public boolean isHorizontal()
    {
        return isHorizontal;
    }
}