package battleship.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class WaitingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private double phase = 0;
    
    public WaitingPanel(int size)
    {
        new AnimationTimer(this, new AnimationTimerCallback() {
            @Override
            public void trigger(double delta_seconds)
            {
                phase += delta_seconds;
                repaint();
            }
        });
        
        setPreferredSize(new Dimension(size, size));
    }
    
    public WaitingPanel()
    {
        /* default size is 64 */
        this(64);
    }
    
    private Shape calcCircle(double p, double offset, double center_x, double center_y, double width)
    {
        double radian = p * Math.PI*2;
        
        double x = Math.cos(radian)*offset + center_x - width/2;
        double y = Math.sin(radian)*offset + center_y - width/2;
        
        return new Ellipse2D.Double(x, y, width, width);
    }
    
    private double adjustPhase(double phase)
    {
        /* window is 0 to 0.5 */
        double window = 0.125;
        
        phase = phase % 1.0;
        phase = phase*(1-window*2) + window;
        
        /* some funky maths stuff */
        
        double cr = (1.0 - Math.cos(window * Math.PI))/2;
        double m = 1.0/(1 - 2*cr);
        double b = -cr*m;
        
        return ((1.0 - Math.cos(phase * Math.PI))/2)*m + b;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = DrawUtils.getGraphics2D(g);
        
        int min_size = Math.min(getWidth(), getHeight());
        
        double circle_width_ratio = 1.0/6;
        double circle_width = min_size * circle_width_ratio;
        double circle_offset = min_size/2 - circle_width/2;
        double center_x = getWidth()/2.0;
        double center_y = getHeight()/2.0;
        
        /* repaint the background, clears previous image */
        
        super.paintComponent(g);
        
        g2d.setColor(new Color(255,0,0,255));
        
        int num_circles = 4;
        double arc_occupy = 0.5;
        
        for (int i = 0; i < num_circles; i++) {
            double sub = (double)i/num_circles * arc_occupy;
            g2d.fill(calcCircle(adjustPhase(phase - sub), circle_offset,
                                center_x, center_y, circle_width*(1-sub)));
        }
    }
}
