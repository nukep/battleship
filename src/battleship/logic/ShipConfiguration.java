package battleship.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ShipConfiguration {
    public class Location {
        public Ship ship;
        public int index;
        public int offset;
        
        public Location(Ship ship, int index, int offset)
        {
            this.ship = ship;
            this.index = index;
            this.offset = offset;
        }
    }
    
    private List<Ship> ships;
    private int columns;
    private int rows;
    
    public ShipConfiguration(int columns, int rows)
    {
        this.columns = columns;
        this.rows = rows;
        ships = new LinkedList<>();
    }
    
    public Iterable<Ship> getShips()
    {
        return Collections.unmodifiableList(ships);
    }
    
    public boolean addShip(int x, int y, int length, boolean isHorizontal)
    {
        if (!hitTestShip(x, y, length, isHorizontal)) {
            // no ship here
            ships.add(new Ship(x, y, length, isHorizontal));
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check if a ship can't be placed here without colliding with other ships.
     * 
     * @param x
     * @param y
     * @param length
     * @param isHorizontal
     * @return True if ship is in the way, false otherwise
     */
    public boolean hitTestShip(int x, int y, int length, boolean isHorizontal)
    {
        for (int i = 0; i < length; i++) {
            int sx = x + (isHorizontal?i:0);
            int sy = y + (isHorizontal?0:i);
            if (hitTest(sx, sy)) {
                // invalid/existing ship in the way
                return true;
            }
        }
        return false;
    }

    /**
     * Get the ship at this location
     * 
     * @param x
     * @param y
     * @return The ship object at this location; null if none exists 
     */
    public Ship getShipAt(int x, int y)
    {
        if (x < 0 || y < 0 || x >= columns || y >= rows) {
            // out of bounds
            return null;
        }
        
        for (Ship s: ships) {
            for (int i = 0; i < s.getLength(); i++) {
                int sx = s.getX() + (s.isHorizontal()?i:0);
                int sy = s.getY() + (s.isHorizontal()?0:i);
                
                if (sx == x && sy == y) {
                    // there's a ship here
                    return s;
                }
            }
        }
        return null;
    }
    
    /**
     * @param x
     * @param y
     * @return The index of the ship at this location; -1 if none exists
     */
    public Location getShipLocation(int x, int y)
    {
        if (x < 0 || y < 0 || x >= columns || y >= rows) {
            // out of bounds
            return null;
        }
        
        int index = 0;
        for (Ship s: ships) {
            for (int i = 0; i < s.getLength(); i++) {
                int sx = s.getX() + (s.isHorizontal()?i:0);
                int sy = s.getY() + (s.isHorizontal()?0:i);
                
                if (sx == x && sy == y) {
                    // there's a ship here
                    return new Location(s, index, i);
                }
            }
            index++;
        }
        return null;
    }

    /**
     * Check if there's a ship at this location
     * 
     * @param x
     * @param y
     * @return True if ship exists/out of bounds, false otherwise
     */
    public boolean hitTest(int x, int y)
    {
        Ship s = getShipAt(x, y);
        return s != null;
    }

    public int getColumns()
    {
        return columns;
    }

    public int getRows()
    {
        return rows;
    }

    public int getShipCount()
    {
        return ships.size();
    }
}
