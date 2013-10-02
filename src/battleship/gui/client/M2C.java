package battleship.gui.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import battleship.common.MessageToClient;
import battleship.common.MessageToServer;
import battleship.gui.client.gamemodes.TargetStrike;

/**
 * The Swing client's MessageToClient implementation.
 * <p>
 * To put it shortly, all messages coming from the server to the client are
 * dealt with here.
 * <p>
 * All calls to the MessageToClient methods are on the Event Dispatcher thread
 * (Swing's GUI thread).
 */
public class M2C implements MessageToClient {
    private MainWindow mainWindow;
    private MessageToServer m2s;
    private UIUpdate uiUpdate;
    private TargetStrike targetStrike;
    private DateFormat chatDateFormat;
    private int target_x, target_y;

    public M2C(MainWindow mainWindow, UIUpdate uiUpdate)
    {
        this.mainWindow = mainWindow;
        this.uiUpdate = uiUpdate;
        this.targetStrike = new TargetStrike(uiUpdate, new TargetStrike.Click() {
            @Override
            public void click(int x, int y)
            {
                target_x = x;
                target_y = y;
                M2C.this.uiUpdate.clearGameMode();
                m2s.strikeSquare(x, y);
                
                M2C.this.uiUpdate.statusMessage("", true);
            }
        });
        chatDateFormat = new SimpleDateFormat("h:mm:ss a");
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
        String dateStr = chatDateFormat.format(date);
        uiUpdate.appendChatbox(dateStr + " - " + message);
    }

    @Override
    public void hitMiss(boolean hit, boolean shipSunk) {
        String message, status;
        
        status = hit ? (shipSunk ? "You sunk their ship":"Hit"):"Miss";
        message = status + "! Opponent's turn";
        
        uiUpdate.clearGameMode();
        uiUpdate.statusMessage(message, true);
        uiUpdate.setTargetHitMiss(target_x, target_y, hit);
    }

    @Override
    public void opponentStrike(int x, int y) {
        boolean hit = uiUpdate.setFleetHitMiss(x, y);
        String status = hit ? "hit":"missed";
        
        uiUpdate.setGameMode(targetStrike);
        uiUpdate.statusMessage(String.format("Opponent %s. Your turn", status), false);
    }

    @Override
    public void gameComplete(boolean youWin) {
        uiUpdate.gameComplete(youWin);
    }

    public void setMessageToServer(MessageToServer m2s) {
        this.m2s = m2s;
    }
}
