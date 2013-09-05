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

public class JoinPanel {
    private class JoinButtonListener implements ActionListener {
        private JoinCallback joinCallback;
        private JTextField addressField;

        private JoinButtonListener(JoinCallback joinCallback,
                                   JTextField addressField)
        {
            this.joinCallback = joinCallback;
            this.addressField = addressField;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            joinCallback.join(addressField.getText());
        }
    }

    private JPanel panel;
    
    public JoinPanel(final JoinCallback joinCallback, JRootPane rootPane)
    {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        JTextField addressField = new JTextField();

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.ipadx = 10;
        c.gridx = 0;
        panel.add(new JLabel("Server IP:"), c);
        c.weightx = 1.0;
        c.gridx = 1;
        panel.add(addressField, c);
        c.gridwidth = 1;
        c.gridy = 1;
        
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(
                new JoinButtonListener(joinCallback, addressField)
        );
        panel.add(joinButton, c);
        
        rootPane.setDefaultButton(joinButton);
    }
    
    public JPanel getPanel()
    {
        return panel;
    }
}
