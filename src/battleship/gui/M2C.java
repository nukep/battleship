package battleship.gui;

import java.util.Date;

import javax.swing.JOptionPane;

import battleship.logic.MessageToClient;

/**
 * The Swing client's MessageToClient implementation.
 * 
 * All calls to the MessageToClient methods are on the Event Dispatcher thread
 * (Swing's GUI thread).
 */
public class M2C implements MessageToClient {
    private MainWindow mainWindow;
    private GameplayPanel gameplay;

    public M2C(MainWindow mainWindow, GameplayPanel gameplay)
    {
        this.mainWindow = mainWindow;
        this.gameplay = gameplay;
    }
    @Override
    public void opponentJoin(String name) {
        gameplay.appendChatbox(name + " joined the game!");
    }

    @Override
    public void disconnected(boolean opponentLeft) {
        JOptionPane.showMessageDialog(null, "Game disconnected");
        mainWindow.switchToJoinPage();
    }

    @Override
    public void turn(boolean yourTurn) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chat(String message, Date date) {
        gameplay.appendChatbox(message);
    }

    @Override
    public void hitMiss(boolean hit) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void opponentStrike(int x, int y) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameComplete(boolean youWin) {
        // TODO Auto-generated method stub
        
    }
}
