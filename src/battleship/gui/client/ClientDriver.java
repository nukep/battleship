package battleship.gui.client;

/**
 * The main driver class of the Client GUI
 *
 */
public class ClientDriver {
    public static void main(String[] args)
    {
        MainWindow.applySwingLookAndFeel();
        MainWindow mw = new MainWindow();
        
        mw.show();
    }
}
