package battleship.gui;

/**
 * Used to signal the UI that a busy operation is undergoing.
 * Currently used for "connecting" and "waiting for opponent".
 * 
 * The methods must be callable from any thread (thread-safe).
 */
public interface BusyInterface {
    public void busy();
    public void unbusy();
}
