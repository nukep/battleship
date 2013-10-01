package battleship.common;

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