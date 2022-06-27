package Model;

public class Foe {
    public Room curRoom;
    public boolean isAwake;
    public boolean isVisible = false;
    public Room treasureRoom;


    public Foe(Room treasureRoom) {
        this.treasureRoom = treasureRoom;
        this.curRoom = treasureRoom;
    }

    public void moveTo(Room newRoom) {
        this.curRoom = newRoom;
    }

    public Room getCurRoom() {
        return curRoom;
    }

    public Player scanForPlayers(Player p1, Player p2) {
        if(p1.hasTreasure) {
            isAwake = true;
            return p1;
        }
        if(p2.hasTreasure) {
            isAwake = true;
            return p2;
        }

        int foeX = treasureRoom.getCoords().get("x");
        int foeY = treasureRoom.getCoords().get("y");

        int maxLeft = 0;
        int maxRight = 8;
        int maxTop = 0;
        int maxBottom = 8;

        if(foeX - 3 >= 0) {
            maxLeft = foeX - 3;
        }
        if(foeX + 3 <= 8) {
            maxRight = foeX + 3;
        }
        if(foeY - 3 >= 0) {
            maxTop = foeY - 3;
        }
        if(foeY + 3 <= 8) {
            maxBottom = foeY + 3;
        }

        int p1Distance = 100;
        int p2Distance = 100;
        for(int y = maxTop; y <= maxBottom; y++) {
            for(int x = maxLeft; x <= maxRight; x++) {
                if(p1.curRoom.getCoords().get("x") == x && p1.curRoom.getCoords().get("y") == y) {
                    p1Distance = (int) Math.sqrt((x - foeX)*(x - foeX) + (y - foeY)*(y-foeY));

                } else if (!p2.eliminated) {
                    if (p2.curRoom.getCoords().get("x") == x && p2.curRoom.getCoords().get("y") == y) {
                        p2Distance = (int) Math.sqrt((x - foeX) * (x - foeX) + (y - foeY) * (y - foeY));
                    }
                }
            }

        }

        if(p1Distance == 100 && p2Distance == 100) {
            isAwake = false;
            return null;
        } else if(p2Distance < p1Distance) {
            isAwake = true;
            return p2;
        } else {
            isAwake = true;
            return p1;
        }

    }

}
