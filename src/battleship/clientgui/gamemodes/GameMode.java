package battleship.clientgui.gamemodes;

import java.awt.Graphics2D;

import battleship.clientgui.BoardPanelDraw;

/**
 * A GameMode represents a state of the BoardPanel control.
 * In this case, it's used to transition from fleet configuration, to nothing,
 * and then to target strike (your turn).
 * 
 * It's essentially an alternative to using if() statements in the BoardPanel
 * class to determine game state, which I consider to be more rigid.
 *
 */
public interface GameMode {
    public void draw(Graphics2D g2d, BoardPanelDraw target, BoardPanelDraw fleet);
    
    public void targetBoxClick(int x, int y, boolean primary);
    public void targetBoxHover(int x, int y);
    public void targetBoxOut();
    
    public void fleetBoxClick(int x, int y, boolean primary);
    public void fleetBoxHover(int x, int y);
    public void fleetBoxOut();
}