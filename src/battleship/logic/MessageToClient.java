package battleship.logic;

import java.util.Date;

public interface MessageToClient {
    /**
     * You are now paired with an opponent.
     * 
     * @param name Name of your adversary
     */
    public void opponentJoin(String name);
    public void disconnected(boolean opponentLeft);
    /**
     * It is now someone's turn.
     * This is also called for the first time when both the players' fleets are
     * configured.
     * 
     * @param yourTurn True if it's your turn, false if it's the opponent's turn
     */
    public void turn(boolean yourTurn);
    /**
     * Client receives a chat message.
     * Note: The client always receives their own messages.
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
     * The opponent made a strike.
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
