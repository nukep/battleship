package battleship.logic;

import java.net.Socket;

public class NetworkPlayer {
    private Socket socket;
    private Player player;
    
    public NetworkPlayer(Socket socket)
    {
        this.socket = socket;
    }
}