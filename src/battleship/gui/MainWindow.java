package battleship.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import battleship.client.NetClient;

public class MainWindow {
    private final class JoinConnectBusy implements BusyInterface {
        private final BusyInterface busy;
        private Connect c;

        private JoinConnectBusy(BusyInterface busy) {
            this.busy = busy;
        }

        @Override
        public void busy() {
            busy.busy();
        }

        @Override
        public void unbusy() {
            NetClient client = c.getNetClient();
            
            if (client == null) {
                JOptionPane.showMessageDialog(null, c.getException());
            } else {
                // connection successful
            }
            busy.unbusy();
        }
        
        public void setConnect(Connect c)
        {
            this.c = c;
        }
    }

    private final class Something implements MapInterface {
        @Override
        public void boxActivate(int x, int y) {
            System.out.printf("%d x %d\n", x, y);
        }
    }

    private JFrame frame;
    private JoinCallback joinCallback;
    
    public static void applySwingLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
        }
        // If there are (non-critical) exceptions, we want to know what they are
        
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public MainWindow()
    {
        frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        
        joinCallback = new JoinCallback() {
            @Override
            public void join(String playerName, String address,
                             BusyInterface busy)
            {
                Connect c;
                JoinConnectBusy b;
                
                b = new JoinConnectBusy(busy);
                c = new Connect(playerName, address, b, null);
                
                b.setConnect(c);
                
                new Thread(c).start();
            }
        };
        
        frame.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        JoinPanel jp = new JoinPanel(joinCallback, frame.getRootPane());
        FleetMapPanel dp = new FleetMapPanel(new Something());
        
        jp.setPreferredSize(new Dimension(320, 240));
        
        frame.add(jp, c);
        //frame.add(dp);
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
}
