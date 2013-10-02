package battleship.gui.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    
    public interface SetupServer {
        public void setupServer();
    }
    
    private static final long serialVersionUID = 1L;

    private JoinPanel joinPanel;
    
    public WelcomePanel(JoinPanel.JoinCallback joinCallback,
                        final SetupServer setupServer)
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
        
        c.gridy++;
        
        JButton setupServerButton = new JButton("Set up server");
        setupServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setupServer.setupServer();
            }
        });
        
        this.add(setupServerButton, c);
    }

    public JButton getDefaultButton()
    {
        return joinPanel.getDefaultButton();
    }

    public BusyListener getBusyListener()
    {
        return joinPanel;
    }
}
