package battleship.server;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import battleship.common.GameSettings;
import battleship.common.Player;

class NetMatchmaker {
    private LinkedList<NetGame> activeGames;
    private NetGame waitingGame;
    private GameSettings gameSettings;
    
    public NetMatchmaker(GameSettings gameSettings)
    {
        this.gameSettings = gameSettings;
        activeGames = new LinkedList<>();
        waitingGame = null;
    }
    
    public synchronized NetGamePlayerHandle connect(NetConnection conn,
            Player player)
            throws InterruptedException
    {
        NetGame game = null;
        boolean firstPlayer;
        
        firstPlayer = waitingGame == null;
        
        if (firstPlayer) {
            // create a new game
            game = new NetGame(conn, player, gameSettings);
            // start its thread
            game.start();
            waitingGame = game;
        } else {
            // an existing game is waiting for a player
            game = waitingGame;
            waitingGame = null;

            activeGames.add(game);
            game.addSecondConnection(conn, player);
        }
        
        if (firstPlayer) {
            // block the thread until the other player joins
            this.wait();
            return game.getPlayerHandle(0);
        } else {
            // unblock the firstPlayer thread
            this.notifyAll();
            return game.getPlayerHandle(1);
        }
    }
    
    public synchronized void disconnect(NetConnection conn)
    {
        if (waitingGame != null && waitingGame.hasConnection(conn)) {
            waitingGame.close();
            waitingGame = null;
        } else {
            Iterator<NetGame> it = activeGames.iterator();
            
            while (it.hasNext()) {
                NetGame g = it.next();
                if (g.hasConnection(conn)) {
                    g.close();
                    it.remove();
                    break;
                }
            }
        }
    }

    public synchronized int getID(NetConnection conn)
    {
        // TODO
        return 0;
    }

    public synchronized void closeAll()
    {
        if (waitingGame != null) {
            waitingGame.close();
        }
        
        for (NetGame g : activeGames) {
            g.close();
        }
    }
}