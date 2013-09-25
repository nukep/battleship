package battleship.gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class MainWindow {
    private final class JoinConnect implements ConnectInterface {
        private final BusyInterface busy;

        private JoinConnect(BusyInterface busy) {
            this.busy = busy;
        }

        @Override
        public void connect(Connect c)
        {
            busy.unbusy();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    switchToGameplay();
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
        frame.setSize(640, 480);
        m2c = new M2C();
        
        joinCallback = new JoinCallback() {
            @Override
            public void join(String playerName, String address,
                             BusyInterface busy)
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
        
        JoinPanel jp = new JoinPanel(joinCallback, frame.getRootPane());
        jp.setPreferredSize(new Dimension(320, 240));
        joinPage.add(jp, c);
        
        GameplayPanel gameplayPage = new GameplayPanel();
        
        fc.add(joinPage, CARD_JOINPAGE);
        fc.add(gameplayPage, CARD_GAMEPLAY);
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
    
    public void switchToJoinPage()
    {
        CardLayout l = (CardLayout)frame.getContentPane().getLayout();
        l.show(frame.getContentPane(), CARD_JOINPAGE);
    }
    
    public void switchToGameplay()
    {
        CardLayout l = (CardLayout)frame.getContentPane().getLayout();
        l.show(frame.getContentPane(), CARD_GAMEPLAY);
    }
}
