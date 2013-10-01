package battleship.clientgui.gamemodes;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import battleship.clientgui.MapPanelDraw;
import battleship.clientgui.UIUpdate;
import battleship.logic.ShipConfiguration;

public class ConfigureFleet implements GameMode {
    private int ship_x, ship_y;
    private boolean hover;
    private boolean horizontal;
    private List<Integer> remainingShips;
    private int currentShipIndex;

    private ShipConfiguration ships;
    private UIUpdate update;
    private GameModeFinished finished;
    
    public ConfigureFleet(ShipConfiguration ships, UIUpdate update,
                          GameModeFinished finished)
    {
        this.ships = ships;
        this.update = update;
        this.finished = finished;
        
        this.hover = false;
        this.horizontal = true;
        this.remainingShips = new LinkedList<>();
        remainingShips.add(5);
        remainingShips.add(4);
        remainingShips.add(3);
        remainingShips.add(3);
        remainingShips.add(2);
        this.currentShipIndex = 0;
    }

    @Override
    public void draw(Graphics2D g2d, MapPanelDraw target, MapPanelDraw fleet)
    {
        int ascent = g2d.getFontMetrics().getAscent();
        int y = 40;
        target.color(0, 0, 0);
        g2d.drawString("LMB - Place ship", 10, y);
        y += ascent;
        g2d.drawString("RMB - Rotate ship", 10, y);
        y += ascent*2;

        g2d.drawString("Remaining ships", 10, y);
        y += ascent;
        
        int shipHeight = 20;
        int i = 0;
        for (Integer shipLength: remainingShips) {
            if (i == currentShipIndex) {
                target.color(0, 0, 0);
            } else {
                target.color(128, 128, 128);
            }
            
            g2d.fillRect(10, y, shipHeight*shipLength, shipHeight);
            y += shipHeight + 5;
            i++;
        }
        
        // draw ship overlay
        if (hover) {
            boolean valid;
            int length = remainingShips.get(currentShipIndex);
            valid = !ships.hitTestShip(ship_x, ship_y, length, horizontal);
            
            if (valid) {
                target.color(0, 0, 0, 64);
            } else {
                target.color(255, 0, 0, 64);
            }
            
            fleet.ship(ship_x, ship_y, length, horizontal);
        }
    }

    /* target map is unused */
    @Override
    public void targetBoxClick(int x, int y, boolean primary) {}

    @Override
    public void targetBoxHover(int x, int y) {}

    @Override
    public void targetBoxOut() {}

    @Override
    public void fleetBoxClick(int x, int y, boolean primary)
    {
        if (primary) {
            // place ship
            gridToShip(x, y);
            int length = remainingShips.get(currentShipIndex);
            boolean placed;
            placed = this.ships.addShip(ship_x, ship_y, length, horizontal);
            update.update();
            
            if (placed) {
                shipPlaced();
            }
        } else {
            // switch orientation
            this.horizontal = !this.horizontal;
            gridToShip(x, y);
            update.update();
        }
    }

    @Override
    public void fleetBoxHover(int x, int y) {
        hover = true;
        gridToShip(x, y);
        update.update();
    }

    @Override
    public void fleetBoxOut() {
        hover = false;
        update.update();
    }
    
    /**
     * Make the ship overlay within the board and centred to the mouse
     * 
     * @param x
     * @param y
     */
    private void gridToShip(int x, int y)
    {
        int length = remainingShips.get(currentShipIndex);
        if (horizontal) {
            ship_x = x - (length-1)/2;
            ship_y = y;
            
            if (ship_x < 0) {
                ship_x = 0;
            }
            
            if (ship_x+length >= ships.getColumns()) {
                ship_x = ships.getColumns() - length;
            }
        } else {
            ship_x = x;
            ship_y = y - (length-1)/2;
            
            if (ship_y < 0) {
                ship_y = 0;
            }
            
            if (ship_y+length >= ships.getRows()) {
                ship_y = ships.getRows() - length;
            }
        }
    }
    
    private void shipPlaced()
    {
        remainingShips.remove(currentShipIndex);
        
        if (remainingShips.isEmpty()) {
            finished.finished();
        }
    }
}
