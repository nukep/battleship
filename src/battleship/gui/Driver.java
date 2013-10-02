package battleship.gui;

public class Driver {
    public static void main(String[] args)
    {
        MainWindow.applySwingLookAndFeel();
        MainWindow mw = new MainWindow();
        
        mw.show();
    }
}
