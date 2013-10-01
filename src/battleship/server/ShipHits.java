package battleship.server;

import battleship.common.Ship;
import battleship.common.ShipConfiguration;

public class ShipHits {
    private boolean[][] shipHits;
    private ShipConfiguration shipConfiguration;
    public ShipHits(ShipConfiguration shipConfiguration)
    {
        this.shipHits = new boolean[shipConfiguration.getShipCount()][];
        this.shipConfiguration = shipConfiguration;
        
        int i = 0;
        for (Ship s: shipConfiguration.getShips()) {
            shipHits[i] = new boolean[s.getLength()];
            i++;
        }
    }
    
    /**
     * @param x
     * @param y
     * @return True if you sunk a ship
     */
    public boolean strike(int x, int y)
    {
        ShipConfiguration.Location loc;
        loc = shipConfiguration.getShipLocation(x, y);
        
        if (loc != null) {
            shipHits[loc.index][loc.offset] = true;
            
            boolean sunk = true;
            for (boolean hit: shipHits[loc.index]) {
                sunk &= hit;
            }
            
            return sunk;
        } else {
            // no ship there
            return false;
        }
    }
    
    public boolean isDefeated()
    {
        boolean defeated = true;
        for (boolean[] hitArray: shipHits) {
            for (boolean hit: hitArray) {
                defeated &= hit;
            }
        }
        return defeated;
    }
}
