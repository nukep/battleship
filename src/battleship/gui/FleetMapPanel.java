package battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

class Coord {
    public double x, y;
}

class Transform {
    private double top_l, bottom_l;
    private double top_r, bottom_r;
    private double top_w, bottom_w;
    private double top_y, height;
    // Restrictions: top_w != 0
    
    public Transform(double top_l, double bottom_l,
                     double top_r, double bottom_r,
                     double top_y, double bottom_y)
    {
        this.top_l = top_l;
        this.bottom_l = bottom_l;
        
        this.top_r = top_r;
        this.bottom_r = bottom_r;
        this.top_w = top_l + top_r;
        this.bottom_w = bottom_l + bottom_r;
        
        this.top_y = top_y;
        this.height = bottom_y - top_y;
    }
    
    private double lerp(double a, double b, double p)
    {
        return (b-a)*p + a;
    }
    
    /* x/y range from 0..1 */
    public Coord transform(double x, double y)
    {
        Coord c = new Coord();
        
        double a = getY(y);
        double l = lerp(top_l, bottom_l, a);
        double r = lerp(top_r, bottom_r, a);

        c.x = lerp(l, r, x);
        c.y = a * height + top_y;
        
        return c;
    }
    
    /* returns Coord range from 0..1 */
    public Coord transformInverse(double x, double y)
    {
        Coord c = new Coord();
        
        c.y = getP(y);
        
        double a = getY(c.y);
        double l = lerp(top_l, bottom_l, a);
        double r = lerp(top_r, bottom_r, a);
        
        c.x = (x - l)/(r - l);
        
        return c;
    }
    
    private double getY(double p)
    {        
        return ((Math.pow(bottom_w / top_w, p) - 1)*top_w)
                 / (bottom_w - top_w);
    }
    
    private double getP(double y)
    {
        return Math.log((y - top_y)*(bottom_w - top_w)/height / top_w + 1)
                / Math.log(bottom_w/top_w);
    }
}

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
        }
    
        @Override
        public void mouseDragged(MouseEvent e) {
            // TODO Auto-generated method stub
            
        }
    }

    private static final long serialVersionUID = 1L;
    
    private MapInterface gridInterface;
    private Transform tr;
    private int grid_columns = 10;
    private int grid_rows = 10;
    
    private static void setAntialias(Graphics2D g)
    {
        /* turns on anti-aliasing (lines appear smoother) */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public FleetMapPanel(MapInterface gridInterface)
    {
        this.gridInterface = gridInterface;
        tr =  new Transform(0, 50, 250, 600, 200, 400);
        
        addMouseListener(new Mouse());
        addMouseMotionListener(new MouseMotion());
    }
	
    @Override
    public void paintComponent(Graphics g)
    {
        /* allows access to 2D functionality (casting is apparently safe) */
        Graphics2D g2d = (Graphics2D)g;
        setAntialias(g2d);
        
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.black);
        
        for (int i = 0; i < grid_columns + 1; i++) {
            Coord a, b;
            a = tr.transform((double)i / grid_columns, 0);
            b = tr.transform((double)i / grid_columns, 1);
            
            g2d.drawLine((int)Math.round(a.x), (int)Math.round(a.y),
                         (int)Math.round(b.x), (int)Math.round(b.y));
        }
        
        for (int i = 0; i < grid_rows + 1; i++) {
            Coord a, b;
            a = tr.transform(0, (double)i / grid_rows);
            b = tr.transform(1, (double)i / grid_rows);
            
            g2d.drawLine((int)Math.round(a.x), (int)Math.round(a.y),
                         (int)Math.round(b.x), (int)Math.round(b.y));
        }
        
        int[] xPoly = new int[4];
        int[] yPoly = new int[4];
        
        {
            int x = 4;
            int y = 4;
            double px = (double)x / grid_columns;
            double py = (double)y / grid_rows;
            double qx = (double)1 / grid_columns;
            double qy = (double)1 / grid_rows;
            
            Coord[] c = new Coord[4];
            c[0] = tr.transform(px, py);
            c[1] = tr.transform(px+qx, py);
            c[2] = tr.transform(px+qx, py+qy);
            c[3] = tr.transform(px, py+qy);
            for (int i = 0; i < 4; i++) {
                xPoly[i] = (int)Math.round(c[i].x);
                yPoly[i] = (int)Math.round(c[i].y);
            }
        }
        
        g2d.fillPolygon(new Polygon(xPoly, yPoly, xPoly.length));
    }
}
