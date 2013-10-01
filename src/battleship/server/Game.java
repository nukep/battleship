package battleship.server;

import java.util.Date;

import battleship.common.MessageToClient;
import battleship.common.MessageToServer;
import battleship.common.Player;
import battleship.common.ShipConfiguration;

/**
 * The Game class is responsible for maintaining the game state between two
 * players.
 *
 */
public class Game {    
    class GameState {
        private PlayerState ps1, ps2;
        
        public GameState(MessageToClient m2c_p1, MessageToClient m2c_p2,
                         Player p1, Player p2)
        {
            ps1 = new PlayerState(m2c_p1, p1);
            ps2 = new PlayerState(m2c_p2, p2);
            
            ps1.m2s = new PlayerInput(this, ps1, ps2);
            ps2.m2s = new PlayerInput(this, ps2, ps1);
        }
        
        public void configureFleet(ShipConfiguration ships, PlayerState p)
        {
            p.ships = ships;
            p.shipHits = new ShipHits(ships);
            
            if (ps1.ships != null && ps2.ships != null) {
                // both players' fleets are configured
                startFirstTurn();
            }
        }
        
        public MessageToServer getMessageToServer(int playerNumber)
        {
            if (playerNumber == 0) {
                return ps1.m2s;
            } else {
                return ps2.m2s;
            }
        }
        
        private void startFirstTurn()
        {
            // randomly select whose turn is first
            boolean player1 = Math.random() > 0.5;
            
            ps1.m2c.firstTurn(player1);
            ps2.m2c.firstTurn(!player1);
        }
    }

    private GameState gameState;

    public Game(MessageToClient m2c_p1, MessageToClient m2c_p2,
                Player p1, Player p2)
    {
        this.gameState = new GameState(m2c_p1, m2c_p2, p1, p2);
    }
    
    public MessageToServer getMessageToServer(int playerNumber)
    {
        return gameState.getMessageToServer(playerNumber);
    }
}

class PlayerState {
    public MessageToClient m2c;
    public PlayerInput m2s;
    
    public Player player;
    public ShipConfiguration ships;
    public ShipHits shipHits;
    
    public PlayerState(MessageToClient m2c,  Player player)
    {
        this.m2c = m2c;
        this.player = player;
    }
}

class PlayerInput implements MessageToServer {
    private Game.GameState gameState;
    private PlayerState you, opponent;
    
    public PlayerInput(Game.GameState game,
                       PlayerState player_you, PlayerState player_opponent)
    {
        this.gameState = game;
        this.you = player_you;
        this.opponent = player_opponent;
    }
    
    @Override
    public void connect(String name)
    {
        opponent.m2c.opponentJoin(name);
    }

    @Override
    public void chat(String message)
    {
        Date now = new Date();
        String new_message;
        
        new_message = String.format("%s: %s", you.player.getName(), message);
        
        // send the chat message to yourself and your opponent
        you.m2c.chat(new_message, now);
        opponent.m2c.chat(new_message, now);
    }

    @Override
    public void configureFleet(ShipConfiguration shipConfiguration)
    {
        gameState.configureFleet(shipConfiguration, you);
    }

    @Override
    public void strikeSquare(int x, int y)
    {
        // check opponent's ship configuration
        boolean hit = opponent.ships.hitTest(x, y);
        boolean shipSunk, winningMove;
        
        shipSunk = opponent.shipHits.strike(x, y);
        winningMove = opponent.shipHits.isDefeated();
        
        you.m2c.hitMiss(hit, shipSunk);
        opponent.m2c.opponentStrike(x, y);
        
        if (winningMove) {
            // you won, opponent lost
            you.m2c.gameComplete(true);
            opponent.m2c.gameComplete(false);
        }
    }
}