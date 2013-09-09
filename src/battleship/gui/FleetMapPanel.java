package battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class FleetMapPanel extends JPanel {
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
            Coord c = tr.transformInverse(arg0.getX(), arg0.getY());
            int x = (int)Math.floor(c.x*grid_columns);
            int y = (int)Math.floor(c.y*grid_rows);
            if (x >= 0 && y >= 0 && x < grid_columns && y < grid_rows) {
                gridInterface.boxActivate(x, y);
            }
        }
    }

    private class MouseMotion implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            // TODO Auto-generated method stub
            Coord c = tr.transformInverse(e.getX(), e.getY());
            mouse_x = (int)Math.floor(c.x*grid_columns);
            mouse_y = (int)Math.floor(c.y*grid_rows);
            
            repaint();
        }
    
        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
    }

    private static final long serialVersionUID = 1L;
    
    private MapInterface gridInterface;
    private MapTransform tr;
    private int grid_columns = 10;
    private int grid_rows = 10;
    private int mouse_x, mouse_y;
    
    private static void setAntialias(Graphics2D g)
    {
        /* turns on anti-aliasing (lines appear smoother) */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public FleetMapPanel(MapInterface gridInterface)
    {
        this.gridInterface = gridInterface;
        tr =  new MapTransform(150, 450, 0, 600, 200, 400);
        
        addMouseListener(new Mouse());
        addMouseMotionListener(new MouseMotion());
    }
    
    private void drawMapSqaure(Graphics2D g2d, int x, int y)
    {
        double px = (double)x / grid_columns;
        double py = (double)y / grid_rows;
        double qx = (double)1 / grid_columns;
        double qy = (double)1 / grid_rows;
        
        Coord[] c = new Coord[4];
        c[0] = tr.transform(px, py);
        c[1] = tr.transform(px+qx, py);
        c[2] = tr.transform(px+qx, py+qy);
        c[3] = tr.transform(px, py+qy);
        
        Path2D.Double dp = new Path2D.Double();
        dp.moveTo(c[0].x, c[0].y);
        for (int i = 1; i < 4; i++) {
            dp.lineTo(c[i].x, c[i].y);
        }
        dp.closePath();
    
        g2d.fill(dp);
    }
    
    private void drawMapGrid(Graphics2D g2d)
    {
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
	
    @Override
    public void paintComponent(Graphics g)
    {
        /* allows access to 2D functionality (casting is apparently safe) */
        Graphics2D g2d = (Graphics2D)g;
        setAntialias(g2d);
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(new Color(255,0,0));
        
        drawMapGrid(g2d);
        
        g2d.setColor(Color.black);
        drawMapSqaure(g2d, mouse_x, mouse_y);
    }
}
