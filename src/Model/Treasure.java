package Model;

public class Treasure {
    public Room treasureRoom;
    public Room curRoom;
    public boolean isVisible = false;

    public Treasure(Room treasureRoom) {
        this.curRoom = treasureRoom;
        this.treasureRoom = treasureRoom;
    }
    public void moveTo(Room toRoom) {

        curRoom = toRoom;
    }
}
