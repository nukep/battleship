package battleship.gui;

import java.util.Date;

import battleship.logic.MessageToClient;

public class M2C implements MessageToClient {
    @Override
    public void opponentJoin(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void opponentLeave() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void turn(boolean yourTurn) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chat(String message, Date date) {
        // TODO Auto-generated method stub
        
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
