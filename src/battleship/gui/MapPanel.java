package battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import javax.swing.JPanel;

import battleship.logic.ShipConfiguration;

class PathTransform {
    private MapTransform tr;
    private int columns;
    private int rows;
    private Path2D.Double path;
    double translate_x, translate_y;

    public PathTransform(MapTransform tr, int columns, int rows)
    {
        this.tr = tr;
        this.columns = columns;
        this.rows = rows;
        this.path = new Path2D.Double();
        this.translate_x = 0;
        this.translate_y = 0;
    }
    
    public void translate(double x, double y)
    {
        translate_x = x;
        translate_y = y;
    }
    
    public void moveTo(double x, double y)
    {
        Coord c = tr.transform((x + translate_x) / columns,
                               (y + translate_y) / rows);
        path.moveTo(c.x, c.y);
    }
    
    public void lineTo(double x, double y)
    {
        Coord c = tr.transform((x + translate_x) / columns,
                               (y + translate_y) / rows);
        path.lineTo(c.x, c.y);
    }
    
    public void closePath()
    {
        path.closePath();
    }
    
    public Path2D.Double getPath()
    {
        return path;
    }
}

public class MapPanel extends JPanel {
    class Mouse implements MouseListener {
        @Override
        public void mouseReleased(MouseEvent arg0) {
        }
    
        @Override
        public void mousePressed(MouseEvent arg0) {
        }
    
        @Override
        public void mouseExited(MouseEvent arg0) {
        }
    
        @Override
        public void mouseEntered(MouseEvent arg0) {
        }
    
        @Override
        public void mouseClicked(MouseEvent arg0) {
            activateMapInterface(arg0.getX(), arg0.getY(),
                                 target_tr, target_grid);
            activateMapInterface(arg0.getX(), arg0.getY(),
                                 fleet_tr, fleet_grid);
        }
    }

    private class MouseMotion implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
        }
    
        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
    }

    private static final long serialVersionUID = 1L;
    
    private MapInterface target_grid, fleet_grid;
    private MapTransform target_tr, fleet_tr;
    private ShipConfiguration shipConfiguration;
    private int grid_columns = 10;
    private int grid_rows = 10;
    
    public MapPanel(MapInterface target_grid, MapInterface fleet_grid,
                    ShipConfiguration shipConfiguration)
    {
        this.target_grid = target_grid;
        this.fleet_grid = fleet_grid;
        this.shipConfiguration = shipConfiguration;
        
        addMouseListener(new Mouse());
        addMouseMotionListener(new MouseMotion());
    }
    
    private void calculateTransforms(int width, int height)
    {
        double box_w = 500;
        double box_h = 550;
        
        double scale = height/box_h;
        double x_off = (width - box_w*scale)/2;
        double y_off = 0.0;
        
        double[] target_arr = {80, 420, 100, 400, 0, 290};
        double[] fleet_arr  = {100, 400, 0, 500, 300, 550};
        
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
    
    private void drawMapSqaure(Graphics2D g2d, MapTransform tr,
                               int x, int y, double inset)
    {
        double px = (x+inset) / grid_columns;
        double py = (y+inset) / grid_rows;
        double qx = (x+1-inset) / grid_columns;
        double qy = (y+1-inset) / grid_rows;
        
        Coord[] c = new Coord[4];
        c[0] = tr.transform(px, py);
        c[1] = tr.transform(qx, py);
        c[2] = tr.transform(qx, qy);
        c[3] = tr.transform(px, qy);
        
        Path2D.Double dp = new Path2D.Double();
        dp.moveTo(c[0].x, c[0].y);
        
        for (int i = 1; i < 4; i++) {
            dp.lineTo(c[i].x, c[i].y);
        }
        
        dp.closePath();
    
        g2d.fill(dp);
    }
    
    private void drawMapGrid(Graphics2D g2d, MapTransform tr)
    {
        PathTransform pt = new PathTransform(tr, 1, 1);
        
        g2d.setColor(new Color(224,224,255));
        
        pt.moveTo(0, 0);
        pt.lineTo(1, 0);
        pt.lineTo(1, 1);
        pt.lineTo(0, 1);
        pt.closePath();
        g2d.fill(pt.getPath());
        
        g2d.setColor(new Color(64,64,128));
        
        // draw column lines (vertical-ish)
        for (int i = 0; i < grid_columns + 1; i++) {
            Coord a, b;
            a = tr.transform((double)i / grid_columns, 0);
            b = tr.transform((double)i / grid_columns, 1);

            Line2D.Double l = new Line2D.Double(a.x, a.y, b.x, b.y);
            g2d.draw(l);
        }
        
        // draw row lines (horizontal)
        for (int i = 0; i < grid_rows + 1; i++) {
            Coord a, b;
            a = tr.transform(0, (double)i / grid_rows);
            b = tr.transform(1, (double)i / grid_rows);

            Line2D.Double l = new Line2D.Double(a.x, a.y, b.x, b.y);
            g2d.draw(l);
        }
    }
    
    private void drawShip(Graphics2D g2d, MapTransform tr, int x, int y, int length, boolean isHorizontal)
    {
        double inset = 0.25;
        
        PathTransform pt = new PathTransform(tr, grid_columns, grid_rows);
        pt.translate(x,  y);
        pt.moveTo(inset, inset);
        
        if (isHorizontal) {
            pt.lineTo(length-inset, inset);
            pt.lineTo(length-inset, 1-inset);
            pt.lineTo(inset, 1-inset);
        } else {
            pt.lineTo(1-inset, inset);
            pt.lineTo(1-inset, length-inset);
            pt.lineTo(inset, length-inset);
        }
        pt.closePath();
        
        g2d.fill(pt.getPath());
    }
    
    private String coordToText(int x, int y)
    {
        return String.format("%c%d", (char)('A'+y), x+1);
    }
	
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = DrawUtils.getGraphics2D(g);
        
        super.paintComponent(g);
        
        calculateTransforms(getWidth(), getHeight());
        
        g2d.setColor(new Color(64, 64, 64));
        
        drawMapGrid(g2d, target_tr);
        drawMapGrid(g2d, fleet_tr);
        drawShip(g2d, fleet_tr, 1, 5, 5, false);
        drawShip(g2d, fleet_tr, 2, 5, 4, true);
        drawShip(g2d, fleet_tr, 9, 1, 2, false);

        g2d.setColor(new Color(0,0,0,192));
        drawMapSqaure(g2d, target_tr, 1, 4, 0.0);
        drawMapSqaure(g2d, fleet_tr, 1, 4, 0.0);
        g2d.setColor(new Color(255,0,0,192));
        drawMapSqaure(g2d, target_tr, 2, 4, 0.0);
        drawMapSqaure(g2d, fleet_tr, 1, 5, 0.0);
        drawMapSqaure(g2d, fleet_tr, 1, 6, 0.0);
    }
    
    public void activateMapInterface(int m_x, int m_y,
                                     MapTransform tr, MapInterface i)
    {
        Coord c = tr.transformInverse(m_x, m_y);
        int x = (int)Math.floor(c.x*grid_columns);
        int y = (int)Math.floor(c.y*grid_rows);
        if (x >= 0 && y >= 0 && x < grid_columns && y < grid_rows) {
            i.boxActivate(x, y);
        }
    }
}
