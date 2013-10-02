package battleship.clientgui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The first panel to be seen by the user.
 * Contains the logo and JoinPanel.
 */
public class WelcomePanel extends JPanel {
    private class TitlePanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        public TitlePanel()
        {
            JLabel title = new JLabel("Battleship");
            title.setFont(new Font(null, Font.BOLD, 40));
            add(title);
        }
        
    }
    private static final long serialVersionUID = 1L;

    private JoinPanel joinPanel;
    
    public WelcomePanel(JoinPanel.JoinCallback joinCallback)
    {
        super(new GridBagLayout());
        joinPanel = new JoinPanel(joinCallback);
        joinPanel.setPreferredSize(new Dimension(320, 240));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        TitlePanel titlePanel = new TitlePanel();
        
        this.add(titlePanel, c);

        c.fill = GridBagConstraints.NONE;
        c.gridy++;
        this.add(joinPanel, c);
    }

    public JButton getDefaultButton()
    {
        return joinPanel.getDefaultButton();
    }
}
