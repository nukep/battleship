package battleship.server;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

class NetMatchmaker {
    private LinkedList<NetGame> activeGames;
    private NetGame waitingGame;
    private Map<NetConnection, NetGame> connectionGameMap;
    
    public NetMatchmaker()
    {
        activeGames = new LinkedList<>();
        connectionGameMap = new TreeMap<>();
        waitingGame = null;
    }
    
    public synchronized NetGamePlayerHandle connect(NetConnection conn)
            throws InterruptedException
    {
        NetGame game = null;
        boolean firstPlayer;
        
        firstPlayer = waitingGame == null;
        
        if (firstPlayer) {
            // create a new game
            game = new NetGame(conn);
            // start its thread
            new Thread(game).start();
            waitingGame = game;
        } else {
            // an existing game is waiting for a player
            game = waitingGame;
            waitingGame = null;

            activeGames.add(game);
            game.addSecondConnection(conn);
            
            this.notifyAll();
        }
        
        connectionGameMap.put(conn, game);
        
        if (firstPlayer) {
            // block the thread until the other player joins
            this.wait();
            return game.getPlayerHandle(0);
        } else {
            return game.getPlayerHandle(1);
        }
    }
    
    public synchronized void disconnect(NetConnection conn)
    {
        NetGame game = connectionGameMap.get(conn);
        // TODO
        
        // give the remaining player an option to play another game
    }
}