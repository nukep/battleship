package battleship.gui;

/**
 * The main entry point of the application
 *
 */
public class Driver {
    public static void main(String[] args)
    {
        MainWindow.applySwingLookAndFeel();
        MainWindow mw = new MainWindow();
        
        mw.show();
    }
}
