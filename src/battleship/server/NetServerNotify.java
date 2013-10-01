package battleship.server;

public interface NetServerNotify {
    public void gameStarted(NetGame game, NetGameNotify notify);
}
