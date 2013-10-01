package battleship.clientgui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * The "waiting" animation, represented by the orange spinning circles.
 *
 */
public class WaitingPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    /* the animation progress; number of seconds elapsed */
    private double phase = 0;
    
    public WaitingPanel(int size)
    {
        new AnimationTimer(this, new AnimationTimer.TimerCallback() {
            @Override
            public void trigger(double delta_seconds)
            {
                phase += delta_seconds;
                repaint();
            }
        });
        
        setPreferredSize(new Dimension(size, size));
        
        /* every time the panel is made visible, reset the phase */
        
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent arg0) {
                phase = 0;
            }
            
            @Override
            public void ancestorRemoved(AncestorEvent arg0) {
            }
            
            @Override
            public void ancestorMoved(AncestorEvent arg0) {
            }
        });
    }
    
    public WaitingPanel()
    {
        /* default size is 64 */
        this(64);
    }
    
    private Shape calcCircle(double p, double offset,
                             double center_x, double center_y, double width)
    {
        double radian = p * Math.PI*2;
        
        double x = Math.cos(radian)*offset + center_x - width/2;
        double y = Math.sin(radian)*offset + center_y - width/2;
        
        return new Ellipse2D.Double(x, y, width, width);
    }
    
    /**
     * Converts a linear 0..1 function to a sinusoidal easing function
     * 
     * @param phase A value from 0..1
     * @return The adjusted phase value
     */
    private double adjustPhase(double phase)
    {
        /* window is 0.0 to 0.5 (non-inclusive)
         * window is the "non-intensity" of the easing function.
         * 0.0 is the most extreme (cosine with no adjustments)
         * 0.5 approaches a line. literally assigning 0.5+ is undefined! */
        double window = 1.0/16;
        
        phase = (phase + 1.0) % 1.0;
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
        
        g2d.setColor(new Color(255,128,0));
        
        int num_circles = 4;
        double arc_occupy = 0.5;
        
        for (int i = 0; i < num_circles; i++) {
            double sub = (double)i/num_circles * arc_occupy;
            g2d.fill(calcCircle(adjustPhase(phase - sub), circle_offset,
                                center_x, center_y, circle_width*(1-sub)));
        }
    }
}
