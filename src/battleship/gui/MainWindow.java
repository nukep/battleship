package battleship.gui;

import java.awt.Container;
import java.awt.GridBagLayout;

import javax.swing.*;

import battleship.client.Connect;
import battleship.client.NetClient;
import battleship.common.GameSettings;
import battleship.common.MessageToServer;
import battleship.gui.client.BusyListener;
import battleship.gui.client.GameplayPanel;
import battleship.gui.client.JoinPanel;
import battleship.gui.client.M2C;
import battleship.gui.client.NetClientDispatchToSwing;
import battleship.gui.client.UIUpdate;
import battleship.gui.client.WelcomePanel;
import battleship.gui.server.SetupServerPanel;

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
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    M2C m2c = new M2C(MainWindow.this, c);

                    NetClient.Dispatcher dispatcher = new NetClientDispatchToSwing(m2c);
                    c.start(dispatcher);
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
    private SetupServerPanel setupServerPage;
    
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
        
        WelcomePanel.SetupServer setupServer = new WelcomePanel.SetupServer() {
            @Override
            public void setupServer() {
                MainWindow.this.switchToSetupServer();
            }
        };
        
        Container fc = frame.getContentPane();
        fc.setLayout(new GridBagLayout());
        
        welcomePage = new WelcomePanel(joinCallback, setupServer);
        setupServerPage = new SetupServerPanel();
        
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
    
    public UIUpdate switchToGameplay(String playerName,
            MessageToServer m2s, M2C m2c, GameSettings gameSettings)
    {
        GameplayPanel gameplayPanel;
        gameplayPanel = new GameplayPanel(playerName, gameSettings);
        
        m2c.setMessageToServer(m2s);
        gameplayPanel.setMessageToServer(m2s);

        frame.setContentPane(gameplayPanel);
        frame.getRootPane().setDefaultButton(gameplayPanel.getDefaultButton());
        frame.revalidate();
        
        return gameplayPanel.getUIUpdate();
    }
    
    public void switchToSetupServer()
    {
        frame.setContentPane(setupServerPage);
        frame.getRootPane().setDefaultButton(null);
        frame.revalidate();
    }
}
