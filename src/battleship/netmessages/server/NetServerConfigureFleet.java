package battleship.netmessages.server;

import java.io.Serializable;
import java.util.ArrayList;

import battleship.common.MessageToServer;
import battleship.common.ShipConfiguration;
import battleship.netmessages.MessageNetServer;

public class NetServerConfigureFleet implements MessageNetServer {
    private static final long serialVersionUID = 1L;
    
    /**
     * We define a serializable ship class for transmission, instead of using
     * the battleship.logic.Ship class. But why?
     * 
     * Game logic is simply not in the same domain as the networking protocol.
     * I'd rather not let "implements Serializable" infect every part of the
     * source code, where it's not needed.
     */
    private class Ship implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public int x, y, length;
        public boolean isHorizontal;
    }

    private int columns;
    private int rows;
    private ArrayList<Ship> ships;

    public NetServerConfigureFleet(ShipConfiguration shipConfiguration)
    {
        this.columns = shipConfiguration.getColumns();
        this.rows = shipConfiguration.getRows();
        this.ships = new ArrayList<>();
        
        for (battleship.common.Ship sh: shipConfiguration.getShips()) {
            Ship ship = new Ship();
            ship.x = sh.getX();
            ship.y = sh.getY();
            ship.length = sh.getLength();
            ship.isHorizontal = sh.isHorizontal();
            
            ships.add(ship);
        }
    }
    
    @Override
    public void toServer(MessageToServer s)
    {
        ShipConfiguration shipConfiguration;
        shipConfiguration = new ShipConfiguration(columns, rows);
        
        for (Ship sh: ships) {
            shipConfiguration.addShip(sh.x, sh.y, sh.length, sh.isHorizontal);
        }
        
        s.configureFleet(shipConfiguration);
    }
}
