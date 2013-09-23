package battleship.server;

class NetGame {
    private NetConnection p1, p2;
    private Game game;
    
    public NetGame(NetConnection p1, NetConnection p2)
    {
        this.p1 = p1;
        this.p2 = p2;
        this.game = new Game(p1, p2,
                             p1.getAssignMessageToServer(),
                             p2.getAssignMessageToServer());
    }
}
