package battleship.gui.client;

import battleship.gui.client.gamemodes.GameMode;

public interface UIUpdate {
    public void update();
    
    public void setGameMode(GameMode gameMode);
    public void clearGameMode();
    
    public void statusMessage(String message, boolean busy);
    public void statusClear();
    
    public void appendChatbox(String message);
    
    public void setTargetHitMiss(int x, int y, boolean hit);
    public boolean setFleetHitMiss(int x, int y);

    public void gameComplete(boolean youWin);
}
