package battleship.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import battleship.logic.MessageToServer;

public class GameplayPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextArea chatBox;
    private JTextField chatTextField;
    private JButton chatSendButton;
    
    private MessageToServer m2s;
    
    private JPanel initChatlinePanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        chatTextField = new JTextField();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        
        panel.add(chatTextField, c);
        
        chatSendButton = new JButton("Send");
        chatSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                String message = chatTextField.getText();
                chatTextField.setText("");
                m2s.chat(message);
            }
        });
        
        c.gridx++;
        c.gridy = 0;
        c.weightx = 0.0;
        
        panel.add(chatSendButton, c);
        
        return panel;
    }

    public GameplayPanel()
    {
        super(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        
        this.add(new MapPanel(new MapInterface() {
            @Override
            public void boxActivate(int x, int y) {
                System.out.printf("target: %d x %d\n", x, y);
            }
        }, new MapInterface() {
            
            @Override
            public void boxActivate(int x, int y)
            {
                System.out.printf("fleet: %d x %d\n", x, y);
            }
        }), c);
        
        c.weighty = 0.0;
        c.gridy++;
        
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setRows(4);
        
        this.add(new JScrollPane(chatBox), c);
        
        c.gridy++;
        
        JPanel chatLinePanel = initChatlinePanel();
        this.add(chatLinePanel, c);
    }
    
    public void setMessageToServer(MessageToServer m2s)
    {
        this.m2s = m2s;
    }

    public void appendChatbox(String message)
    {
        chatBox.append(message + "\n");
        chatBox.updateUI();
        
        // scroll to the bottom of the text box
        chatBox.setCaretPosition(chatBox.getDocument().getLength());
    }

    public JButton getDefaultButton()
    {
        return chatSendButton;
    }
}
