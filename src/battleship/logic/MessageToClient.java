package battleship.logic;

import java.util.Date;

public interface MessageToClient {
    /**
     * You are now paired with an opponent. The game can start.
     * 
     * @param name Name of your adversary
     * @param yourTurnFirst True if you go first, false if opponent goes first
     */
    public void opponentJoin(String name, boolean yourTurnFirst);
    /**
     * Client receives a chat message
     * 
     * @param message The whole message, typically including the player name
     *                or server status
     * @param date    When the message was received on the server
     */
    public void chat(String message, Date date);
    /**
     * Opponent (server) responds whether the last strike hit or miss.
     * 
     * @param hit True on hit, false on miss
     */
    public void hitMiss(boolean hit);
    /**
     * The opponent made a strike. It's now your turn.
     * 
     * @param x
     * @param y
     */
    public void opponentStrike(int x, int y);
    /**
     * The game is complete.
     * 
     * @param youWin True if you win, false if opponent wins
     */
    public void gameComplete(boolean youWin);
}
