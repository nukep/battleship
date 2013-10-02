package battleship.gui.client.gamemodes;

import java.awt.Graphics2D;

import battleship.gui.client.BoardPanelDraw;
import battleship.gui.client.UIUpdate;

/**
 * The TargetStrike game mode is active when it's the player's turn to strike.
 *
 */
public class TargetStrike implements GameMode {
    public interface Click {
        public void click(int x, int y);
    }
    
    private UIUpdate update;
    private Click finished;
    
    private boolean hover;
    private int target_x, target_y;

    public TargetStrike(UIUpdate update, Click finished)
    {
        this.update = update;
        this.finished = finished;
    }
    
    @Override
    public void draw(Graphics2D g2d, BoardPanelDraw target, BoardPanelDraw fleet)
    {
        if (hover) {
            target.color(25, 255, 255, 128);
            target.square(target_x, target_y, 0.0);
        }
    }

    @Override
    public void targetBoxClick(int x, int y, boolean primary)
    {
        hover = false;
        finished.click(x, y);
    }

    @Override
    public void targetBoxHover(int x, int y)
    {
        hover = true;
        target_x = x;
        target_y = y;
        update.update();
    }

    @Override
    public void targetBoxOut()
    {
        hover = false;
        update.update();
    }

    @Override
    public void fleetBoxClick(int x, int y, boolean primary) {}

    @Override
    public void fleetBoxHover(int x, int y) {}

    @Override
    public void fleetBoxOut() {}
}
