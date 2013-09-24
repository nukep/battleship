package battleship.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class JoinPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private class JoinButtonListener implements ActionListener {
        private JoinCallback joinCallback;
        private JTextField playerNameField, addressField;

        private JoinButtonListener(JoinCallback joinCallback,
                                    JTextField playerNameField,
                                    JTextField addressField)
        {
            this.joinCallback = joinCallback;
            this.playerNameField = playerNameField;
            this.addressField = addressField;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            joinCallback.join(playerNameField.getText(),
                              addressField.getText());
        }
    }
    
    private WaitingPanel wp;
    
    public JoinPanel(final JoinCallback joinCallback, JRootPane rootPane)
    {
        JTextField playerNameField = new JTextField();
        JTextField addressField = new JTextField();

        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(
                new JoinButtonListener(joinCallback,
                                       playerNameField, addressField)
        );
        
        setLayout(new GridBagLayout());
        
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
        
        c.gridx = 0;
        c.weightx = 0;
        c.gridy++;
        wp = new WaitingPanel();
        add(wp, c);
        
        rootPane.setDefaultButton(joinButton);
    }
}

interface JoinCallback {
    public void join(String playerName, String address);
}
