package battleship.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import battleship.gui.gamemodes.ConfigureFleet;
import battleship.gui.gamemodes.GameMode;
import battleship.gui.gamemodes.GameModeFinished;
import battleship.logic.HitMissMap;
import battleship.logic.MessageToServer;
import battleship.logic.ShipConfiguration;

public class GameplayPanel extends JPanel implements UIUpdate {
    private static final long serialVersionUID = 1L;
    
    private JTextArea chatBox;
    private JTextField chatTextField;
    private JButton chatSendButton;
    private MapPanel mapPanel;
    private StatusPanel statusPanel;
    
    private MessageToServer m2s;
    private ShipConfiguration shipConfiguration;
    private HitMissMap targetHitMiss;
    private HitMissMap fleetHitMiss;
    
    public GameplayPanel()
    {
        super(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        
        this.targetHitMiss = new HitMissMap(10, 10);
        this.fleetHitMiss  = new HitMissMap(10, 10);
        this.shipConfiguration = new ShipConfiguration(10, 10);
        
        this.mapPanel = new MapPanel(shipConfiguration, targetHitMiss, fleetHitMiss);
        
        this.add(mapPanel, c);
        
        c.weighty = 0.0;
        c.gridy++;
        
        statusPanel = new StatusPanel();
        this.add(statusPanel, c);
        
        c.gridy++;
        
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setRows(4);
        
        this.add(new JScrollPane(chatBox), c);
        
        c.gridy++;
        
        JPanel chatLinePanel = initChatlinePanel();
        this.add(chatLinePanel, c);
        

        GameMode gameMode = new ConfigureFleet(shipConfiguration, this,
            new GameModeFinished() {
                @Override
                public void finished() {
                    mapPanel.clearGameMode();
                    statusMessage("Waiting for other player", true);
                    m2s.configureFleet(shipConfiguration);
                }
            }
        );
        
        this.mapPanel.setGameMode(gameMode);
        statusMessage("Hello! Place your ships on the board above to begin.", false);
    }

    public JButton getDefaultButton()
    {
        return chatSendButton;
    }

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

    public void setMessageToServer(MessageToServer m2s)
    {
        this.m2s = m2s;
    }

    @Override
    public void update()
    {
        mapPanel.repaint();
    }

    @Override
    public void setGameMode(GameMode gameMode)
    {
        mapPanel.setGameMode(gameMode);
    }

    @Override
    public void clearGameMode()
    {
        mapPanel.clearGameMode();
    }

    @Override
    public void statusMessage(String message, boolean busy)
    {
        statusPanel.statusMessage(message, busy);
    }

    @Override
    public void statusClear()
    {
        statusPanel.statusClear();
    }

    @Override
    public void appendChatbox(String message)
    {
        chatBox.append(message + "\n");
        chatBox.updateUI();
        
        // scroll to the bottom of the text box
        chatBox.setCaretPosition(chatBox.getDocument().getLength());
    }

    @Override
    public void setTargetHitMiss(int x, int y, boolean hit)
    {
        targetHitMiss.setHitMiss(x, y, hit);
        mapPanel.repaint();
    }

    @Override
    public void setFleetHitMiss(int x, int y)
    {
        boolean hit = shipConfiguration.hitTest(x, y);
        
        fleetHitMiss.setHitMiss(x, y, hit);
        mapPanel.repaint();
    }
}

class StatusPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JLabel statusLabel;
    private WaitingPanel waitingPanel;
    
    public StatusPanel()
    {
        super(new GridBagLayout());
        
        int height = 48;
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        
        c.weightx = 1.0;
        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font(null, Font.BOLD, 20));
        this.add(statusLabel, c);
        
        c.gridx++;
        
        c.weightx = 0.0;
        waitingPanel = new WaitingPanel(height);
        waitingPanel.setVisible(false);
        this.add(waitingPanel, c);
        
        this.setPreferredSize(new Dimension(0, height));
    }
    
    public void statusMessage(String message, boolean busy)
    {
        statusLabel.setText(message);
        waitingPanel.setVisible(busy);
    }
    
    public void statusClear()
    {
        statusLabel.setText("");
        waitingPanel.setVisible(false);
    }
}
