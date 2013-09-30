package battleship.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WelcomePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JoinPanel joinPanel;
    
    public WelcomePanel(JoinCallback joinCallback)
    {
        super(new GridBagLayout());
        joinPanel = new JoinPanel(joinCallback);
        joinPanel.setPreferredSize(new Dimension(320, 240));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        this.add(joinPanel, c);
    }

    public JButton getDefaultButton()
    {
        return joinPanel.getDefaultButton();
    }
}
