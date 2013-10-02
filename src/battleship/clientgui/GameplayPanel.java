package battleship.clientgui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import battleship.client.HitMissMap;
import battleship.clientgui.gamemodes.ConfigureFleet;
import battleship.clientgui.gamemodes.GameMode;
import battleship.common.GameConstants;
import battleship.common.MessageToServer;
import battleship.common.ShipConfiguration;

/**
 * The Gameplay panel contains everything needed for playing the game.
 * It includes a BoardPanel (the two main boards), and chat controls.
 *
 */
public class GameplayPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private interface PlayAgain {
        public void playAgain(boolean yes);
    }
    
    private class StatusPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        private JLabel statusLabel;
        private WaitingPanel waitingPanel;
        private JPanel playAgainPanel;
        
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
            
            c.gridx = 0;
            c.gridy++;
            c.fill = GridBagConstraints.NONE;
            
            playAgainPanel = new JPanel(new GridLayout());
            
            JButton yesButton, noButton;
            
            yesButton = new JButton("Yes");
            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0)
                {
                    GameplayPanel.this.playAgain.playAgain(true);
                }
            });
            
            noButton = new JButton("No");
            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0)
                {
                    GameplayPanel.this.playAgain.playAgain(false);
                }
            });
            
            playAgainPanel.add(yesButton);
            playAgainPanel.add(noButton);
            playAgainPanel.setVisible(false);
            
            this.add(playAgainPanel, c);
            
            this.setPreferredSize(new Dimension(0, height));
        }
        
        public void statusMessage(String message, boolean busy)
        {
            statusLabel.setText(message);
            waitingPanel.setVisible(busy);
        }
        
        public void showPlayAgain()
        {
            playAgainPanel.setVisible(true);
        }
        
        public void statusClear()
        {
            statusLabel.setText("");
            waitingPanel.setVisible(false);
        }
    }

    private UIUpdate uiUpdate = new UIUpdate() {
        @Override
        public void update()
        {
            boardPanel.repaint();
        }

        @Override
        public void setGameMode(GameMode gameMode)
        {
            boardPanel.setGameMode(gameMode);
        }

        @Override
        public void clearGameMode()
        {
            boardPanel.clearGameMode();
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
            boardPanel.repaint();
        }

        @Override
        public boolean setFleetHitMiss(int x, int y)
        {
            boolean hit = shipConfiguration.hitTest(x, y);
            
            fleetHitMiss.setHitMiss(x, y, hit);
            boardPanel.repaint();
            
            return hit;
        }

        @Override
        public void gameComplete(boolean youWin)
        {
            String message;
            
            message = youWin ? "You won! Yay." : "You lost the game.";
            
            message += " Play again?";
            
            clearGameMode();
            statusMessage(message, false);
            statusPanel.showPlayAgain();
        }
    };

    private String name;
    private JTextArea chatBox;
    private JTextField chatTextField;
    private JButton chatSendButton;
    private BoardPanel boardPanel;
    private StatusPanel statusPanel;
    
    private MessageToServer m2s;
    private ShipConfiguration shipConfiguration;
    private HitMissMap targetHitMiss;
    private HitMissMap fleetHitMiss;
    private PlayAgain playAgain;
    
    public GameplayPanel(String name)
    {
        super(new GridBagLayout());
        
        this.name = name;
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        
        int you_cols, you_rows, opponent_cols, opponent_rows;
        you_cols = opponent_cols = GameConstants.BOARD_WIDTH;
        you_rows = opponent_rows = GameConstants.BOARD_WIDTH;

        this.shipConfiguration = new ShipConfiguration(you_cols, you_rows);
        this.targetHitMiss = new HitMissMap(opponent_cols, opponent_rows);
        this.fleetHitMiss  = new HitMissMap(you_cols, you_rows);
        
        this.boardPanel = new BoardPanel(shipConfiguration, targetHitMiss, fleetHitMiss);
        
        this.add(boardPanel, c);
        
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

        GameMode gameMode = new ConfigureFleet(shipConfiguration, uiUpdate,
            new ConfigureFleet.Done() {
                @Override
                public void done() {
                    boardPanel.clearGameMode();
                    uiUpdate.statusMessage("Waiting for other player", true);
                    m2s.configureFleet(shipConfiguration);
                }
            }
        );
        
        this.boardPanel.setGameMode(gameMode);
        uiUpdate.statusMessage("Hello! Place your ships on the blue board above to begin.", false);
    
        playAgain = new PlayAgain() {
            @Override
            public void playAgain(boolean yes)
            {
                if (yes) {
                    
                } else {
                    
                }
            }
        };
    }

    public JButton getDefaultButton()
    {
        return chatSendButton;
    }

    public void setMessageToServer(MessageToServer m2s)
    {
        this.m2s = m2s;
    }
    
    public UIUpdate getUIUpdate()
    {
        return uiUpdate;
    }

    private JPanel initChatlinePanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        chatTextField = new JTextField();
    
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.0;
        
        panel.add(new JLabel(name + ": "));
        
        c.gridx++;
        c.weightx = 1.0;
        
        panel.add(chatTextField, c);
        
        chatSendButton = new JButton("Send");
        chatSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                String message = chatTextField.getText();
                if (!message.isEmpty()) {
                    chatTextField.setText("");
                    m2s.chat(message);
                }
            }
        });
        
        c.gridx++;
        c.weightx = 0.0;
        
        panel.add(chatSendButton, c);
        
        // Give the chat text field the default focus
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chatTextField.requestFocus();
            }
        });
        
        return panel;
    }
}
