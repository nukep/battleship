package battleship.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class WaitingPanel extends JPanel {
    private AnimationTimer timer;
    
    int x = 0;
    
    public WaitingPanel()
    {
        timer = new AnimationTimer(new AnimationTimerCallback() {
            @Override
            public void trigger(double delta_seconds)
            {
                x++;
                repaint();
            }
        });
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(new Color(255,0,0));
        g.drawRect(x,0, 10, 10);
    }
}
