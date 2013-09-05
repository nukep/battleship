package battleship.gui;

public class Main {
    public static void main(String[] args)
    {
        MainWindow.applySwingLookAndFeel();
        MainWindow mw = new MainWindow();
        
        mw.show();
    }
}
