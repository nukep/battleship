package battleship.server;

/**
 * NetGameNotify is used to notify the implementing UI of game activity.
 *
 */
public interface NetGameNotify {
    public void turn(boolean player1);
    public void gameFinished();
}
