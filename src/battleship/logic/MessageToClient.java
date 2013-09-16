package battleship.logic;

import java.util.Date;

public interface MessageToClient {
    public void chat(String message, Date date);
    public void hitMiss(boolean hit);
    public void opponentShipSunk();
}
