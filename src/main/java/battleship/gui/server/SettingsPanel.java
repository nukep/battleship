package battleship.gui.server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import battleship.common.GameConstants;
import battleship.common.NetConstants;

/**
 * The Settings panel contains controls for the games to be run on the server
 *
 */
class SettingsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JSpinner boardSizeSpinner;
    private JTextField shipLengthsField;
    private JTextField portField;
    
    public SettingsPanel()
    {
        super(new GridBagLayout());
        
        boardSizeSpinner = new JSpinner(new SpinnerNumberModel(GameConstants.DEFAULT_BOARD_WIDTH, 5, 30, 1));
        shipLengthsField = new JTextField(GameConstants.DEFAULT_SHIP_LENGTHS);
        shipLengthsField.setColumns(15);
        portField = new JTextField(Integer.toString(NetConstants.DEFAULT_PORT));
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 10;
        c.anchor = GridBagConstraints.LINE_START;
        
        add(new JLabel("Board size:"), c);
        
        c.gridx++;
        add(boardSizeSpinner, c);
        
        c.gridx = 0;
        c.gridy++;
        
        add(new JLabel("Ship lengths:"), c);
        c.gridx++;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(shipLengthsField, c);
        
        c.gridx = 0;
        c.gridy++;
        
        add(new JLabel("Port:"), c);
        c.gridx++;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(portField, c);
    }
    
    public int getBoardSize()
    {
        return (Integer)boardSizeSpinner.getValue();
    }
    
    /**
     * @param shipLengths
     * @return True if form text is valid, false if invald
     */
    public boolean getShipLengths(List<Byte> shipLengths)
    {
        @SuppressWarnings("resource")
        Scanner k = new Scanner(shipLengthsField.getText());
        k.useDelimiter(",");
        
        int boardSize = getBoardSize();
        
        while (k.hasNext()) {
            String numStr = k.next().trim();
            
            // allow empty strings (e.g. from parsing "5, 4, , 3,")
            if (numStr.isEmpty())
                continue;
            
            boolean invalid = false;
            
            try {
                byte b = Byte.parseByte(numStr);
                
                // invalid if length is less than 1, or longer than board
                invalid |= b < 1 || b > boardSize;
                
                shipLengths.add(b);
            } catch (NumberFormatException e) {
                // not a number
                invalid |= true;
            }
            
            if (invalid) {
                shipLengths.clear();
                return false;
            }
        }
        
        if (shipLengths.size() < 1) {
            // no ships
            return false;
        }
        
        return true;
    }
    
    public int getPort()
    {
        String portStr = portField.getText();
        int port;
        
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            port = NetConstants.DEFAULT_PORT;
        }
        
        return port;
    }
    
    public void setEnabled(boolean enabled)
    {
        boardSizeSpinner.setEnabled(enabled);
        shipLengthsField.setEnabled(enabled);
        portField.setEnabled(enabled);
    }
}