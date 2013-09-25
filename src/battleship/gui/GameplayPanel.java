package battleship.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameplayPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public GameplayPanel()
    {
        super(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        
        this.add(new FleetMapPanel(new MapInterface() {
            @Override
            public void boxActivate(int x, int y) {
                System.out.printf("%d x %d\n", x, y);
            }
        }), c);
        
        c.gridy++;
        c.weighty = 0.0;
        
        this.add(new JTextField(), c);
    }
}
