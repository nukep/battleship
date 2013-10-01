package battleship.clientgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * The AnimationTimer class provides a timer with a consistent frame rate.
 *
 */
public class AnimationTimer {
    // We're using a constant frame rate for all animations
    final static int FPS = 60;
    
    private Timer timer;
    private boolean firstTick;
    
    public AnimationTimer(JComponent component, final AnimationTimerCallback cb)
    {
        int delay = 1000 / FPS;
        timer = new Timer(delay, new ActionListener() {
            
            private long prev_ms;
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                long cur_ms = System.currentTimeMillis();
                double diff;
                
                if (firstTick) {
                    // prev_ms is not set yet - no difference can be calculated
                    
                    firstTick = false;
                    diff = 1.0 / FPS;
                } else {
                    long diff_ms = cur_ms - prev_ms;
                    diff = (double)diff_ms / 1000;
                }
                cb.trigger(diff);
                
                prev_ms = cur_ms;
            }
        });
        
        /* start and stop the timer when the component is shown or hidden */
        
        component.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent arg0) {
                /* shown / added */
                
                firstTick = true;
                timer.start();
            }

            @Override
            public void ancestorRemoved(AncestorEvent arg0) {
                /* hidden / removed */
                
                timer.stop();
            }
            
            @Override
            public void ancestorMoved(AncestorEvent arg0) {
            }
        });
    }
}

interface AnimationTimerCallback {
    public void trigger(double delta_seconds);
}
