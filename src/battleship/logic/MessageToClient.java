package battleship.logic;

import java.util.Date;

public interface MessageToClient {
    /**
     * Client receives a chat message
     * 
     * @param message The whole message, typically including the player name
     *                or server status
     * @param date    When the message was received on the server
     */
    public void chat(String message, Date date);
    /**
     * Opponent player (server) responds whether the last strike hit or miss
     * 
     * @param hit True on hit, false on miss
     */
    public void hitMiss(boolean hit);
    public void opponentShipSunk();
}
