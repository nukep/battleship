package battleship.logic;

import java.util.Date;

public class Game {
    private MessageToClient p1;
    private MessageToClient p2;
    
    private PlayerInput m2s1, m2s2;

    public Game(MessageToClient m2c_p1, MessageToClient m2c_p2,
                Player p1, Player p2)
    {
        this.p1 = m2c_p1;
        this.p2 = m2c_p2;
        
        m2s1 = new PlayerInput(m2c_p1, m2c_p2, p1, p2);
        m2s2 = new PlayerInput(m2c_p2, m2c_p1, p2, p1);
    }
    
    public MessageToServer getMessageToServer(int playerNumber)
    {
        if (playerNumber == 0) {
            return m2s1;
        } else {
            return m2s2;
        }
    }
}

class PlayerInput implements MessageToServer {
    private MessageToClient you, opponent;
    private Player player_you, player_opponent;
    
    public PlayerInput(MessageToClient you, MessageToClient opponent,
                       Player player_you, Player player_opponent)
    {
        this.you = you;
        this.opponent = opponent;
        
        this.player_you = player_you;
        this.player_opponent = player_opponent;
    }
    
    @Override
    public void connect(String name)
    {
        opponent.opponentJoin(name);
    }

    @Override
    public void chat(String message)
    {
        Date now = new Date();
        String new_message;
        
        new_message = String.format("%s: %s", player_you.getName(), message);
        
        // send the chat message to yourself and your opponent
        you.chat(new_message, now);
        opponent.chat(new_message, now);
    }

    @Override
    public void strikeSquare(int x, int y)
    {
        // TODO Auto-generated method stub
        
    }
}