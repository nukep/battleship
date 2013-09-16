package battleship.gui;

import javax.swing.*;

public class MainWindow {
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
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e)
        {
        }
        // If there are (non-critical) exceptions, we want to know what they are
        // Even though printStackTrace is EVIL, we really don't care
        
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
    
    public MainWindow()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        
        joinCallback = new JoinCallback() {
            @Override
            public void join(String playerName, String address)
            {
                JOptionPane.showMessageDialog(null, playerName + "\n" + address);
            }
        };
        
        JoinPanel jp = new JoinPanel(joinCallback, frame.getRootPane());
        FleetMapPanel dp = new FleetMapPanel(new Something());
        
        frame.add(new WaitingPanel());
        //frame.add(jp);
        //frame.add(dp);
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
}
