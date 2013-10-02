package battleship.server;

/**
 * NetServerNotify is used to notify the implementing UI of server activity.
 *
 */
public interface NetServerNotify {
    public void gameStarted(NetGame game);
}
