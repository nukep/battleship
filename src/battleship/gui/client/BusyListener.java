package battleship.gui.client;

/**
 * BusyListener is used to signal the UI that a busy operation is undergoing.
 * <p>
 * Currently used for "connecting" and "waiting for opponent".
 * The methods must be callable from any thread (thread-safe).
 */
public interface BusyListener {
    public void busy();
    public void unbusy();
}
