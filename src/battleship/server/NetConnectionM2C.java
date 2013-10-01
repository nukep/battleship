package battleship.server;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

import battleship.logic.MessageToClient;
import battleship.netmessages.MessageNetClient;
import battleship.netmessages.client.*;

class NetConnectionM2C implements MessageToClient
{
    private BlockingQueue<MessageNetClient> queue;
    
    public NetConnectionM2C(BlockingQueue<MessageNetClient> queue)
    {
        this.queue = queue;
    }
    
    @Override
    public void opponentJoin(String name) {
        queue.add(new NetClientOpponentJoin(name));
    }

    @Override
    public void disconnected(boolean opponentLeft) {
        queue.add(new NetClientDisconnected(opponentLeft));
    }

    @Override
    public void firstTurn(boolean yourTurn) {
        queue.add(new NetClientTurn(yourTurn));
    }

    @Override
    public void chat(String message, Date date) {
        queue.add(new NetClientChat(message, date));
    }

    @Override
    public void hitMiss(boolean hit, boolean shipSunk) {
        queue.add(new NetClientHitMiss(hit, shipSunk));
    }

    @Override
    public void opponentStrike(int x, int y) {
        queue.add(new NetClientOpponentStrike(x, y));
    }

    @Override
    public void gameComplete(boolean youWin) {
        queue.add(new NetClientGameComplete(youWin));
    }
}