package battleship.clientgui;

import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.*;

import battleship.client.Connect;
import battleship.client.NetClient;

/**
 * The MainWindow class is the primary window (frame) used by the Client GUI.
 *
 */
public class MainWindow {
    private final class JoinConnect implements Connect.ConnectListener {
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

    private JFrame frame;
    private JoinPanel.JoinCallback joinCallback;
    private WelcomePanel welcomePage;
    
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
        frame.setSize(600, 600);
        
        joinCallback = new JoinPanel.JoinCallback() {
            @Override
            public void join(String playerName, String address,
                             BusyListener busy)
            {
                Connect c;
                JoinConnect b;
                
                busy.busy();
                b = new JoinConnect(busy);
                c = new Connect(playerName, address, b);
                
                new Thread(c).start();
            }
        };
        
        Container fc = frame.getContentPane();
        fc.setLayout(new GridBagLayout());
        
        welcomePage = new WelcomePanel(joinCallback);
        
        switchToWelcomePage();
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
    
    public void switchToWelcomePage()
    {
        frame.setContentPane(welcomePage);
        frame.getRootPane().setDefaultButton(welcomePage.getDefaultButton());
        frame.revalidate();
    }
    
    public void switchToGameplay(Connect c)
    {
        GameplayPanel gameplayPanel;
        gameplayPanel = new GameplayPanel(c.getPlayerName());
        
        M2C m2c = new M2C(this, gameplayPanel.getUIUpdate());
        NetClient.Dispatcher dispatcher = new NetClientDispatchToSwing(m2c);
        c.start(dispatcher);
        
        m2c.setMessageToServer(c.getMessageToServer());
        gameplayPanel.setMessageToServer(c.getMessageToServer());

        frame.setContentPane(gameplayPanel);
        frame.getRootPane().setDefaultButton(gameplayPanel.getDefaultButton());
        frame.revalidate();
    }
}
