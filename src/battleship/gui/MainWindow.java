package battleship.gui;

import javax.swing.*;

public class MainWindow {
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(640, 480);
        
        joinCallback = new JoinCallback() {
            @Override
            public void join(String address)
            {
                JOptionPane.showMessageDialog(null, address);
            }
        };
        
        JoinPanel p = new JoinPanel(joinCallback, frame.getRootPane());
        DisplayPanel dp = new DisplayPanel();
        
        //frame.add(p.getPanel());
        frame.add(dp.getPanel());
    }
    
    public void show()
    {
        frame.setVisible(true);
    }
}
