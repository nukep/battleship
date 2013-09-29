package battleship.gui.gamemodes;

import java.awt.Graphics2D;

import battleship.gui.MapPanelDraw;

public interface GameMode {
    public void draw(Graphics2D g2d, MapPanelDraw target, MapPanelDraw fleet);
    
    public void targetBoxClick(int x, int y, boolean primary);
    public void targetBoxHover(int x, int y);
    public void targetBoxOut();
    
    public void fleetBoxClick(int x, int y, boolean primary);
    public void fleetBoxHover(int x, int y);
    public void fleetBoxOut();
}
