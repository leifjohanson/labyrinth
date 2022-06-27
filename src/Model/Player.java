package Model;

public class Player {

    public String name;
    public Room curRoom;
    public Room secretRoom;
    public boolean eliminated;
    public boolean hasTreasure;

    public int moveCount;

    public int fatigue;
    public int actualFatigue;

    public Player(boolean eliminated, int fatigue, String name) {
//        this.secretRoom = secretRoom;
//        this.curRoom = secretRoom;
        this.eliminated = eliminated;
        this.fatigue = fatigue;
        this.actualFatigue = fatigue;
        this.name = name;
    }

    public Room getCurRoom() {
        return this.curRoom;
    }

    public Room getSecretRoom() {
        return this.secretRoom;
    }

    public void setSecretRoom(Room secretRoom) {
        this.secretRoom = secretRoom;
        this.curRoom = secretRoom;
    }

    public void moveTo(Room roomPressed) {
        curRoom = roomPressed;
    }

    public boolean isEliminated() {
        return this.eliminated;
    }

    public boolean isValidMove() {
        // check if requestedRoom is valid then return if it is (valid in this case is adjacent to curRoom, dont worry about walls yet)
        return false;
    }

    public void playerHit() {
        actualFatigue -= 2;
        fatigue = actualFatigue;
        moveTo(secretRoom);

        if(hasTreasure) {
            losesTreasure();
        }
        if(actualFatigue < 4) {
            eliminated = true;
        }
    }

    public void getsTreasure() {
        hasTreasure = true;
        fatigue = 4;
    }

    public void losesTreasure() {
        hasTreasure = false;
        fatigue = actualFatigue;
    }
}
