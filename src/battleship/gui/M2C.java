package battleship.gui;

import java.util.Date;

import javax.swing.JOptionPane;

import battleship.gui.gamemodes.GameModeFinished;
import battleship.gui.gamemodes.TargetStrike;
import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;

/**
 * The Swing client's MessageToClient implementation.
 * 
 * All calls to the MessageToClient methods are on the Event Dispatcher thread
 * (Swing's GUI thread).
 */
public class M2C implements MessageToClient {
    private MainWindow mainWindow;
    private MessageToServer m2s;
    private UIUpdate uiUpdate;
    private TargetStrike targetStrike;

    public M2C(MainWindow mainWindow, UIUpdate uiUpdate)
    {
        this.mainWindow = mainWindow;
        this.uiUpdate = uiUpdate;
        this.targetStrike = new TargetStrike(uiUpdate, new GameModeFinished() {
            @Override
            public void finished()
            {
                int x = M2C.this.targetStrike.getX();
                int y = M2C.this.targetStrike.getY();
                
                M2C.this.uiUpdate.clearGameMode();
                m2s.strikeSquare(x, y);
                
                M2C.this.uiUpdate.statusMessage("", true);
            }
        });
    }
    
    @Override
    public void opponentJoin(String name) {
        uiUpdate.appendChatbox(name + " joined the game!");
    }

    @Override
    public void disconnected(boolean opponentLeft) {
        String reason;
        
        reason = opponentLeft ? "Opponent left" : "Lost connection";
        
        JOptionPane.showMessageDialog(null, "Game disconnected - " + reason);
        mainWindow.switchToWelcomePage();
    }

    @Override
    public void firstTurn(boolean yourTurn) {
        if (yourTurn) {
            uiUpdate.setGameMode(targetStrike);
            uiUpdate.statusMessage("Your turn", false);
        } else {
            uiUpdate.clearGameMode();
            uiUpdate.statusMessage("Opponent's turn", true);
        }
    }

    @Override
    public void chat(String message, Date date) {
        uiUpdate.appendChatbox(message);
    }

    @Override
    public void hitMiss(boolean hit) {
        String message;
        
        int x = targetStrike.getX();
        int y = targetStrike.getY();
        
        message = String.format("%s! Opponent's turn", hit?"Hit":"Miss");
        uiUpdate.clearGameMode();
        uiUpdate.statusMessage(message, true);
        uiUpdate.setTargetHitMiss(x, y, hit);
    }

    @Override
    public void opponentStrike(int x, int y) {
        uiUpdate.setGameMode(targetStrike);
        uiUpdate.statusMessage(String.format("%d x %d. Your turn", x, y), false);
        uiUpdate.setFleetHitMiss(x, y);
    }

    @Override
    public void gameComplete(boolean youWin) {
        String message;
        
        message = youWin ? "You won! Yay." : "You lost the game.";
        
        uiUpdate.clearGameMode();
        uiUpdate.statusMessage(message, false);
    }

    public void setMessageToServer(MessageToServer m2s) {
        this.m2s = m2s;
    }
}
