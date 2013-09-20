package battleship.client;

import java.util.Date;

import battleship.logic.MessageToClient;
import battleship.logic.MessageToServer;

public class LocalClient implements MessageToServer {
    private MessageToClient m2c;

    public LocalClient(MessageToClient opponent)
    {
        this.m2c = opponent;
    }

    @Override
    public void chat(String message) {
        m2c.chat(message, new Date());
    }

    @Override
    public void strikeSquare(int x, int y) {
        // TODO
        m2c.hitMiss(false);
    }
}
