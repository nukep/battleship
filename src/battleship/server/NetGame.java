package battleship.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import battleship.logic.Game;
import battleship.logic.MessageToServer;
import battleship.logic.Player;
import battleship.netmessages.MessageNetServer;

class NetGame implements Runnable {
    private Object secondConnectionCondition;
    private NetConnection conn1, conn2;
    private Player player1, player2;
    
    private BlockingQueue<NetGameMessage> serverQueue;
    private Game game;
    
    public NetGame(NetConnection conn1, Player player1)
    {
        this.conn1 = conn1;
        this.conn2 = null;
        this.player1 = player1;
        this.player2 = null;
        
        secondConnectionCondition = new Object();
        this.serverQueue = new LinkedBlockingQueue<>();
        this.game = null;
    }

    @Override
    public void run()
    {
        try {
            // wait until a second connection joins
            synchronized (secondConnectionCondition) {
                while (conn2 == null) {
                    secondConnectionCondition.wait();
                }
            }
            // start the game!
            
            while (true) {
                MessageToServer m2s;
                NetGameMessage netgame_message = serverQueue.take();
                
                m2s = game.getMessageToServer(netgame_message.player);
                netgame_message.message.toServer(m2s);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void addSecondConnection(NetConnection connection, Player player)
    {
        synchronized (secondConnectionCondition) {
            conn2 = connection;
            player2 = player;
            this.game = new Game(conn1, conn2, player1, player2);
            secondConnectionCondition.notify();
        }
    }
    
    public NetGamePlayerHandle getPlayerHandle(int playerNumber)
    {
        return new NetGamePlayerHandle(playerNumber, serverQueue);
    }
}

class NetGameMessage {
    public int player;
    public MessageNetServer message;
    
    public NetGameMessage(int player, MessageNetServer message)
    {
        this.player = player;
        this.message = message;
    }
}

class NetGamePlayerHandle {
    private int playerNumber;
    private BlockingQueue<NetGameMessage> serverQueue;
    
    public NetGamePlayerHandle(int playerNumber,
                               BlockingQueue<NetGameMessage> serverQueue)
    {
        this.playerNumber = playerNumber;
        this.serverQueue = serverQueue;
    }

    public void messageToServer(MessageNetServer message)
    {
        serverQueue.add(new NetGameMessage(playerNumber, message));
    }
}
