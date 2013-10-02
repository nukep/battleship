package battleship.common;

import java.util.Date;

/**
 * Server -> Client
 * <p>
 * The MessageToClient interface is <i>very</i> important.
 * It is used on both the server and client side for message passing.
 * <p>
 * The server uses MessageToClient to send messages to the client.<br>
 * The client uses MessageToClient to receive messages from the server.
 * 
 * @see MessageToServer
 * @see battleship.netmessages.MessageNetClient
 */
public interface MessageToClient {
    /**
     * You are now paired with an opponent.
     * 
     * @param name Name of your adversary
     */
    public void opponentJoin(String name);
    /**
     * The game has been disconnected.
     * 
     * @param opponentLeft True if the opponent decided to leave
     */
    public void disconnected(boolean opponentLeft);
    /**
     * It is now someone's turn.
     * <p>
     * This is also called for the first time when both the players' fleets are
     * configured.
     * 
     * @param yourTurn True if it's your turn, false if it's the opponent's turn
     */
    public void firstTurn(boolean yourTurn);
    /**
     * Client receives a chat message.
     * <p>
     * <i>Note: The client always receives their own messages.</i>
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
     * @param shipSunk True if you sunk their ship
     */
    public void hitMiss(boolean hit, boolean shipSunk);
    /**
     * The opponent made a strike.
     * 
     * @param x
     * @param y
     * @param hit
     */
    public void opponentStrike(int x, int y);
    /**
     * The game is complete.
     * 
     * @param youWin True if you win, false if opponent wins
     */
    public void gameComplete(boolean youWin);
    /**
     * @param boardSize
     * @param shipLengths
     */
    public void settings(GameSettings settings);
}
