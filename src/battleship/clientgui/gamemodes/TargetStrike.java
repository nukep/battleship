package battleship.clientgui.gamemodes;

import java.awt.Graphics2D;

import battleship.clientgui.BoardPanelDraw;
import battleship.clientgui.UIUpdate;

public class TargetStrike implements GameMode {
    private UIUpdate update;
    private GameModeFinished finished;
    
    private boolean hover;
    private int target_x, target_y;

    public TargetStrike(UIUpdate update, GameModeFinished finished)
    {
        this.update = update;
        this.finished = finished;
    }
    
    public int getX()
    {
        return target_x;
    }
    
    public int getY()
    {
        return target_y;
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
        target_x = x;
        target_y = y;
        finished.finished();
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
