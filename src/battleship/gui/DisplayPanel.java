package battleship.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DisplayPanel {
    private JPanel panel;
    
    public DisplayPanel()
    {
        panel = new JPanel();
        BufferedImage b = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = b.createGraphics();
        g.setPaint(Color.pink);
        g.drawRect(5, 5, 100, 100);
        panel.paint(g);
    }
    
    public JPanel getPanel()
    {
        return panel;
    }

}
