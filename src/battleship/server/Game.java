package battleship.server;

import java.util.Date;

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;

public class Game {
    private MessageToClient p1;
    private MessageToClient p2;

    public Game(MessageToClient p1, MessageToClient p2,
                AssignMessageToServer mp1, AssignMessageToServer mp2)
    {
        this.p1 = p1;
        this.p2 = p2;
        
        mp1.assignMessageToServer(new PlayerInput(p1, p2));
        mp2.assignMessageToServer(new PlayerInput(p2, p1));
        
        p1.opponentJoin("P2");
        p2.opponentJoin("P1");
    }
}

class PlayerInput implements MessageToServer {
    private MessageToClient you, opponent;
    
    public PlayerInput(MessageToClient you, MessageToClient opponent)
    {
        this.you = you;
        this.opponent = opponent;
    }
    
    @Override
    public void connect(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chat(String message) {
        Date now = new Date();
        
        you.chat(message, now);
        opponent.chat(message, now);
    }

    @Override
    public void strikeSquare(int x, int y) {
        // TODO Auto-generated method stub
        
    }
}