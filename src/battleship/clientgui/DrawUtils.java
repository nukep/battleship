package battleship.clientgui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class DrawUtils {    
    public static Graphics2D getGraphics2D(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;
        
        /* turns on anti-aliasing (lines appear smoother) */
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        /* sub-pixel precision */
        
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                           RenderingHints.VALUE_STROKE_PURE); 
        
        return g2d;
    }
}
