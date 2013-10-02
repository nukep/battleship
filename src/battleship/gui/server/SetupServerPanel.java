package battleship.gui.server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import battleship.common.GameSettings;
import battleship.server.NetServer;

public class SetupServerPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private SettingsPanel settingsPanel;
    private NetServer netServer;
    private JButton startButton, stopButton;
    private JTextArea logArea;

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
                        netServer = new NetServer(gameSettings, port);
                        
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
        setRunningStatus(false);
    }
}
