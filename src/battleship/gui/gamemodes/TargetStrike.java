package battleship.gui.gamemodes;

import java.awt.Color;
import java.awt.Graphics2D;

import battleship.gui.MapPanelDraw;
import battleship.gui.UIUpdate;

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
    public void draw(Graphics2D g2d, MapPanelDraw target, MapPanelDraw fleet)
    {
        if (hover) {
            g2d.setColor(new Color(0,0,0,64));
            target.square(target_x, target_y, 0.0);
        }
    }

    @Override
    public void targetBoxClick(int x, int y, boolean primary)
    {
        hover = false;
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