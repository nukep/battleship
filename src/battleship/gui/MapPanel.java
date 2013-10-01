package battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.JPanel;

import battleship.gui.gamemodes.GameMode;
import battleship.logic.HitMissMap;
import battleship.logic.Ship;
import battleship.logic.ShipConfiguration;

public class MapPanel extends JPanel {
    private static class GridCoord {
        public int x, y;

        public GridCoord(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        public boolean equals(Object obj)
        {
            if (obj == null) {
                return false;
            } else {
                GridCoord b = (GridCoord)obj;
                return x == b.x && y == b.y;
            }
        }
    }
    
    private class Mouse implements MouseListener {
        @Override
        public void mouseReleased(MouseEvent e) {
            GridCoord tc, fc;
            
            tc = mouseToGridCoord(e.getX(), e.getY(), target_tr);
            fc = mouseToGridCoord(e.getX(), e.getY(), fleet_tr);
            
            boolean primary = e.getButton() == MouseEvent.BUTTON1;
            
            if (tc != null) {
                gameMode.targetBoxClick(tc.x, tc.y, primary);
            }
            if (fc != null) {
                gameMode.fleetBoxClick(fc.x, fc.y, primary);
            }
        }
        
        @Override
        public void mouseExited(MouseEvent e)
        {
            if (MapPanel.this.target_coord != null) {
                gameMode.targetBoxOut();
                MapPanel.this.target_coord = null;
            }
            
            if (MapPanel.this.fleet_coord != null) {
                gameMode.fleetBoxOut();
                MapPanel.this.fleet_coord = null;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
    }

    private class MouseMotion implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            GridCoord tc, fc;
            
            tc = mouseToGridCoord(e.getX(), e.getY(), target_tr);
            fc = mouseToGridCoord(e.getX(), e.getY(), fleet_tr);
            
            if (tc == null) {
                if (MapPanel.this.target_coord != null) {
                    gameMode.targetBoxOut();
                }
            } else if (!tc.equals(MapPanel.this.target_coord)) {
                gameMode.targetBoxHover(tc.x, tc.y);
            }
            
            if (fc == null) {
                if (MapPanel.this.fleet_coord != null) {
                    gameMode.fleetBoxOut();
                }
            } else if (!fc.equals(MapPanel.this.fleet_coord)) {
                gameMode.fleetBoxHover(fc.x, fc.y);
            }
            
            MapPanel.this.target_coord = tc;
            MapPanel.this.fleet_coord = fc;
        }
    
        @Override
        public void mouseDragged(MouseEvent e) {}
    }

    private static final long serialVersionUID = 1L;
    
    private final GameMode NO_GAMEMODE = new GameMode() {
        @Override
        public void targetBoxOut() {}
        @Override
        public void targetBoxHover(int x, int y) {}
        @Override
        public void targetBoxClick(int x, int y, boolean primary) {}
        @Override
        public void fleetBoxOut() {}
        @Override
        public void fleetBoxHover(int x, int y) {}
        @Override
        public void fleetBoxClick(int x, int y, boolean primary) {}
        @Override
        public void draw(Graphics2D g2d, MapPanelDraw target, MapPanelDraw fleet) {}
    };
    
    private MapTransform target_tr, fleet_tr;
    private ShipConfiguration shipConfiguration;
    private HitMissMap targetHitMiss, fleetHitMiss;
    private GameMode gameMode;
    
    private int grid_columns;
    private int grid_rows;
    private GridCoord target_coord, fleet_coord;

    public MapPanel(ShipConfiguration shipConfiguration,
                    HitMissMap targetHitMiss, HitMissMap fleetHitMiss)
    {
        this.shipConfiguration = shipConfiguration;
        this.targetHitMiss = targetHitMiss;
        this.fleetHitMiss = fleetHitMiss;
        
        this.gameMode = NO_GAMEMODE;
        
        this.grid_columns = shipConfiguration.getColumns();
        this.grid_rows = shipConfiguration.getRows();
        calculateTransforms(getWidth(), getHeight());
        
        addMouseListener(new Mouse());
        addMouseMotionListener(new MouseMotion());
    }
    
    public void setGameMode(GameMode gameMode)
    {
        this.gameMode = gameMode;
        repaint();
    }
    
    public void clearGameMode()
    {
        this.gameMode = NO_GAMEMODE;
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = DrawUtils.getGraphics2D(g);
        
        super.paintComponent(g);
        
        calculateTransforms(getWidth(), getHeight());
        
        MapPanelDraw drawTarget, drawFleet;
        drawTarget = new MapPanelDraw(g2d, target_tr, grid_columns, grid_rows, false);
        drawFleet  = new MapPanelDraw(g2d, fleet_tr, grid_columns, grid_rows, true);
        
        drawTarget.grid();
        drawFleet.grid();
        
        g2d.setColor(new Color(64, 64, 128));
        
        for (Ship s: shipConfiguration.getShips()) {
            drawFleet.ship(s.getX(), s.getY(), s.getLength(), s.isHorizontal());
        }
        
        drawHitMiss(targetHitMiss, g2d, drawTarget);
        drawHitMiss(fleetHitMiss, g2d, drawFleet);
        
        gameMode.draw(g2d, drawTarget, drawFleet);
    }

    public GridCoord mouseToGridCoord(int m_x, int m_y, MapTransform tr)
    {
        MapTransform.Coord c = tr.transformInverse(m_x, m_y);
        int x = (int)Math.floor(c.x*grid_columns);
        int y = (int)Math.floor(c.y*grid_rows);
        boolean boxHit = x >= 0 && y >= 0 && x < grid_columns && y < grid_rows;
        
        if (boxHit) {
            return new GridCoord(x, y);
        } else {
            return null;
        }
    }
    
    private void drawHitMiss(HitMissMap hitMissMap,
                             Graphics2D g2d,MapPanelDraw drawTarget)
    {
        int columns = shipConfiguration.getColumns();
        int x = 0;
        int y = 0;
        Iterator<Byte> hisMissIter;
        
        for (hisMissIter = hitMissMap.iterator(); hisMissIter.hasNext(); ) {
            byte state = hisMissIter.next();
            Color color = null;
            
            switch (state) {
            case HitMissMap.HIT:
                color = new Color(255, 0, 0, 192);
                break;
            case HitMissMap.MISS:
                color = new Color(0, 0, 0, 192);
                break;
            }
            
            if (color != null) {
                g2d.setColor(color);
                drawTarget.square(x, y, 0);
            }
            
            x++;
            if (x >= columns) {
                x = 0;
                y++;
            }
        }
        
        assert(x == 0);
        assert(y == shipConfiguration.getRows());
    }

    private void calculateTransforms(int width, int height)
    {
        double box_w = 500;
        double box_h = 580;
        
        double scale = height/box_h;
        double x_off = (width - box_w*scale)/2;
        double y_off = 0.0;
        
        double[] target_arr = {80, 420, 100, 400, 10, 300};
        double[] fleet_arr  = {120, 380, 10, 490, 330, 570};
        
        int i;
        for (i = 0; i < 4; i++) {
            target_arr[i] = target_arr[i]*scale + x_off;
            fleet_arr[i] = fleet_arr[i]*scale + x_off;
        }
        for (; i < 6; i++) {
            target_arr[i] = target_arr[i]*scale + y_off;
            fleet_arr[i] = fleet_arr[i]*scale + y_off;
        }

        target_tr = new MapTransform(target_arr[0], target_arr[1],
                                     target_arr[2], target_arr[3],
                                     target_arr[4], target_arr[5]);
        
        fleet_tr = new MapTransform(fleet_arr[0], fleet_arr[1],
                                    fleet_arr[2], fleet_arr[3],
                                    fleet_arr[4], fleet_arr[5]);
    }
}
