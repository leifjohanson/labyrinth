package Model;

public class GameManager {
    public Room[][] labyrinth = new Room[][]{};

    public Player p1;
    public Player p2;
    public Foe foe;
    public int round;
    public int turn;
    public int turnPerRound;
    public Treasure treasure;

    public boolean started = false;
    public boolean gameOver = false;

    public GameManager() {

    }
    public GameManager(Room[][] labyrinth, Player p1, Player p2, Foe foe, int round, int turn, Treasure treasure) {
        this.labyrinth = labyrinth;
        this.p1 = p1;
        this.p2 = p2;
        this.foe = foe;
        this.round = round;
        this.turn = turn;
        this.turnPerRound = 0;
        this.treasure = treasure;
    }

    public int livePlayerCount() {
        int count = 0;
        if(!p1.isEliminated()) {
            count ++;
        }
        if(!p2.isEliminated()) {
            count ++;
        }
        return count;
    }

}
