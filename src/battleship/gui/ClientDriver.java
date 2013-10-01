package battleship.gui;

public class ClientDriver {
    public static void main(String[] args)
    {
        MainWindow.applySwingLookAndFeel();
        MainWindow mw = new MainWindow();
        
        mw.show();
    }
}
