package battleship.clientgui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JoinPanel extends JPanel implements BusyListener {
    private static final long serialVersionUID = 1L;

    private class JoinButtonListener implements ActionListener {
        private JoinCallback joinCallback;

        private JoinButtonListener(JoinCallback joinCallback)
        {
            this.joinCallback = joinCallback;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String playerName = playerNameField.getText();
            String address = addressField.getText();
            
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter your name");
            } else if (address.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter the server address");
            } else {
                joinCallback.join(playerName, address, JoinPanel.this);
            }
        }
    }
    
    private WaitingPanel wp;
    private JButton joinButton;
    private JTextField playerNameField, addressField;
    
    public JoinPanel(final JoinCallback joinCallback)
    {
        super(new GridBagLayout());
        
        playerNameField = new JTextField();
        addressField = new JTextField("localhost");

        joinButton = new JButton("Join");
        joinButton.addActionListener(new JoinButtonListener(joinCallback));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 10;
        
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Your name:"), c);
        c.gridy++;
        add(new JLabel("Server:"), c);
        
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        add(playerNameField, c);
        c.gridy++;
        add(addressField, c);
        
        // Add a dummy label for padding
        c.weightx = 1.0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy++;
        add(new JLabel(), c);

        c.weightx = 0.5;
        c.gridx = 2;
        add(joinButton, c);

        wp = new WaitingPanel();
        wp.setVisible(false);
        JLabel dummy = new JLabel();
        dummy.setPreferredSize(wp.getPreferredSize());
        
        c.gridx = 0;
        c.weightx = 0;
        c.gridy++;
        add(dummy, c);
        
        c.gridx++;
        add(wp, c);
    }
    
    public JButton getDefaultButton()
    {
        return joinButton;
    }

    @Override
    public void busy() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                joinButton.setEnabled(false);
                playerNameField.setEnabled(false);
                addressField.setEnabled(false);
                wp.setVisible(true);
            }
        });
    }

    @Override
    public void unbusy() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                wp.setVisible(false);
                addressField.setEnabled(true);
                playerNameField.setEnabled(true);
                joinButton.setEnabled(true);
            }
        });
    }
}

interface JoinCallback {
    public void join(String playerName, String address, BusyListener busy);
}
