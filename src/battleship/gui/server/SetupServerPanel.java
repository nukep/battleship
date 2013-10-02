package battleship.gui.server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import battleship.common.GameSettings;
import battleship.common.Player;
import battleship.server.NetGame;
import battleship.server.NetServer;
import battleship.server.NetServerNotify;

/**
 * The SetupServerPanel lets the user configure and start/stop the server
 */
public class SetupServerPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private SettingsPanel settingsPanel;
    private NetServer netServer;
    private JButton startButton, stopButton;
    private JTextArea logArea;
    
    private NetServerNotify netServerNotify = new NetServerNotify() {
        @Override
        public void gameStarted(NetGame game)
        {
            String p1name = game.getPlayer(0).getName();
            String p2name = game.getPlayer(2).getName();
            addToLog(String.format("Game started: %s vs %s", p1name, p2name));
        }
        
        @Override
        public void gameVictor(NetGame game, Player player)
        {
            String p1name = game.getPlayer(0).getName();
            String p2name = game.getPlayer(2).getName();
            String winner = player.getName();
            addToLog(String.format("%s vs %s: %s wins!", p1name, p2name, winner));
        }

        @Override
        public void gameFinished(NetGame game)
        {
            String p1name = game.getPlayer(0).getName();
            String p2name = game.getPlayer(2).getName();
            addToLog(String.format("Game ended: %s vs %s", p1name, p2name));
        }

    };

    public SetupServerPanel()
    {
        super(new GridBagLayout());
        
        settingsPanel = new SettingsPanel();
        
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        logArea = new JTextArea();
        logArea.setEditable(false);
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg)
            {
                ArrayList<Byte> shipLengths = new ArrayList<>();
                int boardSize, port;
                
                if (!settingsPanel.getShipLengths(shipLengths)) {
                    JOptionPane.showMessageDialog(null, "Ship lengths are invalid");
                } else {
                    boardSize = settingsPanel.getBoardSize();
                    port = settingsPanel.getPort();
                    
                    byte[] arr = new byte[shipLengths.size()];
                    
                    int i = 0;
                    for (byte shipLength: shipLengths) {
                        arr[i] = shipLength;
                        i++;
                    }
                    
                    GameSettings gameSettings = new GameSettings(boardSize, arr);
                    
                    try {
                        netServer = new NetServer(gameSettings, port, netServerNotify);
                        
                        new Thread(netServer).start();
                        
                        start();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error starting server: " + e);
                    }
                }
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg)
            {
                netServer.stop();
                netServer = null;
                
                stop();
            }
        });
        
        JPanel startStopPanel = new JPanel(new GridLayout());
        startStopPanel.add(startButton);
        startStopPanel.add(stopButton);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        add(settingsPanel, c);
        
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        add(startStopPanel, c);
        
        c.gridy++;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(logArea), c);
        
        setRunningStatus(false);
    }
    
    private void setRunningStatus(boolean running)
    {
        settingsPanel.setEnabled(!running);
        startButton.setEnabled(!running);
        stopButton.setEnabled(running);
    }
    
    private void start()
    {
        setRunningStatus(true);
    }
    
    private void stop()
    {
        logArea.setText("");
        setRunningStatus(false);
    }
    
    private void addToLog(final String message)
    {
        final SimpleDateFormat chatDateFormat = new SimpleDateFormat("h:mm:ss a");
        final Date date = new Date();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                String m = chatDateFormat.format(date) + ": " + message + "\n";
                logArea.append(m);
                logArea.updateUI();
                
                // scroll to the bottom of the text box
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }
        });
    }
}
