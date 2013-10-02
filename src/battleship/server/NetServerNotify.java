package battleship.server;

import battleship.common.Player;

/**
 * NetServerNotify is used to notify the implementing UI of server activity.
 *
 */
public interface NetServerNotify {
    public void gameStarted(NetGame game);
    public void gameVictor(NetGame game, Player player);
    public void gameFinished(NetGame game);
}
