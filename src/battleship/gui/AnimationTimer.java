package battleship.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * The AnimationTimer class provides a timer with a consistent frame rate.
 *
 */
public class AnimationTimer {
    // We're using a constant frame rate for all animations
    final static int FPS = 60;
    
    private Timer timer;
    
    public AnimationTimer(final AnimationTimerCallback cb)
    {
        int delay = 1000 / FPS;
        timer = new Timer(delay, new ActionListener() {
            {
                firstTick = true;
            }
            
            private long prev_ms;
            private boolean firstTick;
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                long cur_ms = System.currentTimeMillis();
                double diff;
                
                if (firstTick) {
                    // prev_ms is not set yet - no difference can be calculated
                    
                    firstTick = false;
                    diff = (double)1000 / FPS;
                } else {
                    long diff_ms = cur_ms - prev_ms;
                    diff = (double)diff_ms / 1000;
                    
                }
                cb.trigger(diff);
                
                prev_ms = cur_ms;
            }
        });
    }
    
    public void start()
    {
        timer.start();
    }
    
    public void stop()
    {
        timer.stop();
    }
}
