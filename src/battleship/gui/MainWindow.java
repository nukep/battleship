package battleship.gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class MainWindow {
    private final class JoinConnect implements ConnectListener {
        private final BusyListener busy;

        private JoinConnect(BusyListener busy) {
            this.busy = busy;
        }

        @Override
        public void connect(final Connect c)
        {
            busy.unbusy();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    switchToGameplay(c);
                }
            });
        }

        @Override
        public void error(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            busy.unbusy();
        }
    }
    
    private static final String CARD_JOINPAGE = "joinpage";
    private static final String CARD_GAMEPLAY = "gameplay";

    private JFrame frame;
    private JoinCallback joinCallback;
    private JoinPanel joinPanel;
    private GameplayPanel gameplayPanel;
    private M2C m2c;
    
    
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
        frame.setSize(800, 600);
        
        gameplayPanel = new GameplayPanel();
        
        m2c = new M2C(this, gameplayPanel);
        
        joinCallback = new JoinCallback() {
            @Override
            public void join(String playerName, String address,
                             BusyListener busy)
            {
                Connect c;
                JoinConnect b;
                
                busy.busy();
                b = new JoinConnect(busy);
                c = new Connect(playerName, address, b, m2c);
                
                new Thread(c).start();
            }
        };
        
        Container fc = frame.getContentPane();
        
        fc.setLayout(new CardLayout());
        
        JPanel joinPage = new JPanel(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        joinPanel = new JoinPanel(joinCallback);
        joinPanel.setPreferredSize(new Dimension(320, 240));
        joinPage.add(joinPanel, c);
        
        fc.add(joinPage, CARD_JOINPAGE);
        fc.add(gameplayPanel, CARD_GAMEPLAY);
        
        switchToJoinPage();
        
        // TODO - temporary, for testing purposes
        String name = Integer.toString((int)(Math.random()*(1<<16)), 32);
        joinCallback.join(name, "localhost", joinPanel);
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
    
    public void switchToJoinPage()
    {
        CardLayout l = (CardLayout)frame.getContentPane().getLayout();
        l.show(frame.getContentPane(), CARD_JOINPAGE);
        
        frame.getRootPane().setDefaultButton(joinPanel.getDefaultButton());
    }
    
    public void switchToGameplay(Connect c)
    {
        gameplayPanel.setMessageToServer(c.getMessageToServer());
        
        CardLayout l = (CardLayout)frame.getContentPane().getLayout();
        l.show(frame.getContentPane(), CARD_GAMEPLAY);
        
        frame.getRootPane().setDefaultButton(gameplayPanel.getDefaultButton());
    }
}
