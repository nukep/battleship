package battleship.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ShipConfiguration {
    private List<Ship> ships;
    
    public ShipConfiguration()
    {
        ships = new LinkedList<>();
    }
    
    public Iterable<Ship> getShips()
    {
        return Collections.unmodifiableList(ships);
    }
    
    public boolean addShip(int x, int y, int length, boolean isHorizontal)
    {
        for (int i = 0; i < length; i++) {
            int sx = x + (isHorizontal?i:0);
            int sy = y + (isHorizontal?0:i);
            if (hitTest(sx, sy)) {
                // existing ship in the way
                return false;
            }
        }
        
        ships.add(new Ship(x, y, length, isHorizontal));
        
        return true;
    }
    
    private boolean hitTestShip(int x, int y, Ship s)
    {
        for (int i = 0; i < s.getLength(); i++) {
            int sx = s.getX() + (s.isHorizontal()?i:0);
            int sy = s.getY() + (s.isHorizontal()?0:i);
            
            if (sx == x && sy == y) {
                return true;
            }
        }
        
        // ship not there
        return false;
    }
    
    private boolean hitTest(int x, int y)
    {
        for (Ship s: ships) {
            if (hitTestShip(x,  y, s)) {
                return true;
            }
        }
        return false;
    }
}

class Ship {
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
