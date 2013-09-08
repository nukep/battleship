package battleship.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import sun.java2d.pipe.DrawImage;

class Coord {
    public double x, y;
}

class Transform {
    private double top_w, bottom_w;
    // Restrictions: top_w != 0
    
    public double getY(double p)
    {
        // (b/t)^p * t
        
        return Math.pow(bottom_w / top_w, p) * top_w;
        /*
        return (Math.pow(bottom_w, p) * Math.pow(top_w, 1-p) - top_w)
                / (bottom_w - top_w);*/
    }
    
    public double getP(double y)
    {
        // log((b*y)/t - y + 1) / log(b/t)
        
        return Math.log((bottom_w*y)/top_w - y + 1) / Math.log(bottom_w/top_w);
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
            int x = arg0.getX() / grid_box_size - 1;
            int y = arg0.getY() / grid_box_size - 1;
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
    int grid_columns = 10;
    int grid_rows = 10;
    int grid_box_size = 40;
    
    private static void setAntialias(Graphics2D g)
    {
        /* turns on anti-aliasing (lines appear smoother) */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public FleetMapPanel(MapInterface gridInterface)
    {
        this.gridInterface = gridInterface;
        
        addMouseListener(new Mouse());
        addMouseMotionListener(new MouseMotion());
    }
	
    @Override
    public void paintComponent(Graphics g)
    {
        /* allows access to 2D functionality (casting is apparently safe) */
        Graphics2D g2d = (Graphics2D)g;
        setAntialias(g2d);

        double m = 1.2;
        int top_w = 20;
        int bottom_w = 50;
        int center_x = 300;
        int top_y = 0;
        int bottom_y = 200;
        
        for (int i = 0; i < grid_columns + 1; i++) {
            g2d.drawLine((i-grid_columns/2)*top_w + center_x,    top_y,
                         (i-grid_columns/2)*bottom_w + center_x, bottom_y);
        }
        
        double scale = 2;
        double y = 0;
        for (int i = 0; i < grid_rows + 1; i++) {
            g2d.drawLine(0, (int)(y*scale), 600, (int)(y*scale));
            y += Math.pow(m, i);
            
            //y = (Math.pow(m, grid_rows+1)) - 1 / (m - 1);
        }
    }
}
