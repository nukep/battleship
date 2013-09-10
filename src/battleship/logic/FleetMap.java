package battleship.logic;

import java.util.ArrayList;

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
}

public class FleetMap {
    private int columns, rows;
    private ArrayList<Ship> ships;
    
    public FleetMap(int columns, int rows)
    {
        this.columns = columns;
        this.rows    = rows;
        this.ships   = new ArrayList<Ship>();
    }
    
    /**
     * Returns true if invalid/an obstacle exists
     */
    public boolean hitTestShip(int x, int y, int length, boolean isHorizontal)
    {
        boolean hit = false;
        if (isHorizontal) {
            for (int i = 0; i < length; i++) {
                hit |= hitTest(x+i, y);
            }
        } else {
            for (int i = 0; i < length; i++) {
                hit |= hitTest(x, y+i);
            }
        }
        
        return hit;
    }
    
    public boolean hitTest(int x, int y)
    {
        if (x < 0 || y < 0 || x >= columns || y >= rows) {
            // outside of the map - invalid
            return true;
        } else {
            for (Ship s: ships) {
                
            }
            // TODO
            return false;
        }
    }
    
    public boolean placeShip(int x, int y, int length, boolean isHorizontal)
    {
        if (hitTestShip(x, y, length, isHorizontal)) {
            return false;
        } else {
            Ship s = new Ship(x, y, length, isHorizontal);
            
            ships.add(s);
            
            return true;
        }
    }
    
    public void destroyBox(int x, int y)
    {
    }
}
