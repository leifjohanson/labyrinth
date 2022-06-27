package Model;

import View.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LabyrinthDriver {

    private Player p1;
    private Player p2;
    private Foe f;
    private Treasure treasure;
    private Room[][] labyrinth = new Room[9][9];
    private int[][] maze = new int[9][9];
    private int x = 9;
    private int y = 9;

    public void run() {
        // 1. Set view, something along the lines of:
        MapView mv = new MapView();

        // 2. MapViewController handles the rest of initial setup, aka boxes 1-3

        // 3. GenerateLabyrinth() (box 4)

        this.p1 = new Player(true, 8, "p1");
        this.p2 = new Player(true, 8, "p2");

        // 4. Create GameManager constructor (b4)
        initializeLabyrinth();

        GameManager game = new GameManager(labyrinth, p1, p2, f, 0, 0, treasure);

        mv.setMap(labyrinth);
        mv.setGame(game);

        mv.generateView();





    }
    public void initializeLabyrinth() {
        initializeMaze(0,0);

        for (int y = 0; y < 9; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                Room room = new Room(x, y);

                room.setWestWall((maze[x][y] & 8) == 0);
                room.setNorthWall((maze[x][y] & 1) == 0);

                if (x < maze.length - 1)
                {
                    room.setEastWall((maze[x + 1][y] & 8) == 0);
                }

                if (y < maze[0].length - 1)
                {
                    room.setSouthWall((maze[x][y + 1] & 1) == 0);
                }

                labyrinth[x][y] = room;
            }
        }
    }


    public void display() {
        for (int i = 0; i < y; i++) {
            // draw the north edge
            for (int j = 0; j < x; j++) {
                System.out.print((maze[j][i] & 1) == 0 ? "+---" : "+   ");
            }
            System.out.println("+");
            // draw the west edge
            for (int j = 0; j < x; j++) {
                System.out.print((maze[j][i] & 8) == 0 ? "|   " : "    ");
            }
            System.out.println("|");
        }
        // draw the bottom line
        for (int j = 0; j < x; j++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }


    public void initializeMaze(int cx, int cy) {
        DIR[] dirs = DIR.values();
        Collections.shuffle(Arrays.asList(dirs));
        for (DIR dir : dirs) {
            int nx = cx + dir.dx;
            int ny = cy + dir.dy;
            if (between(nx, x) && between(ny, y)
                    && (maze[nx][ny] == 0)) {
                maze[cx][cy] |= dir.bit;
                maze[nx][ny] |= dir.opposite.bit;
                initializeMaze(nx, ny);
            }
        }
//        for(int y = 0; y < 9; y++) {
//            for(int x = 0; x < 9; x++) {
//                labyrinth[x][y] = new Room(x, y);
//            }
//        }
    }

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    private enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
        private final int bit;
        private final int dx;
        private final int dy;
        private DIR opposite;

        // use the static initializer to resolve forward references
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private DIR(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }
    }


//    public Room[][] generateLabyrinth() {
//
//        int[] treasureCoords = pickTreasureRoom();
//
//        for(int y = 0; y < 9; y++) {
//            for(int x = 0; x < 9; x++) {
//                labyrinth[x][y] = new Room(x, y);
//                if(x == treasureCoords[0] && y == treasureCoords[1]) { // if current room being looped through is the
//                    labyrinth[x][y].setTreasureRoom(true);
//                    treasure = new Treasure(labyrinth[x][y]);
//                    f = new Foe(labyrinth[x][y]);
//                    labyrinth[x][y].containsTreasure = true;
//                    System.out.println("Treasure Room has been set at: " + Arrays.toString(treasureCoords) + " " + x + " " + y); // making sure its set
//                } else if (p1.getSecretRoom().getCoords().get("x") == x && p1.getSecretRoom().getCoords().get("y") == y) {
//                    labyrinth[x][y].setP1SecretRoom(true);
//                    System.out.println("Player 1 Secret Room has been set at: " + x + " " + y);
//                } else if (!p2.isEliminated() && (p2.getSecretRoom().getCoords().get("x") == x && p2.getSecretRoom().getCoords().get("y") == y)) {
//                    labyrinth[x][y].setP2SecretRoom(true);
//                    System.out.println("Player 2 Secret Room has been set at: " + x + " " + y);
//                }
//            }
//        }
//
//        return labyrinth;
//    }
//
//    public int[] pickTreasureRoom() {
//        int p1X = this.p1.getCurRoom().getCoords().get("x");
//        int p1Y = this.p1.getCurRoom().getCoords().get("y");
//
//        int p2X = 0;
//        int p2Y = 0;
//        // if p2 exists
//        if(!this.p2.isEliminated()) {
//            p2X = this.p2.getCurRoom().getCoords().get("x");
//            p2Y = this.p2.getCurRoom().getCoords().get("y");
//        } else { // p2 values are set to p1 values if p2 doesn't exist so it doesn't affect foe placement
//            p2X = this.p1.getCurRoom().getCoords().get("x");
//            p2Y = this.p1.getCurRoom().getCoords().get("y");
//        }
//
//        ArrayList<int[]> eligibleCoords = new ArrayList();
//
//        for(int y = 0; y < 9; y ++) {
//            for(int x = 0; x < 9; x++) {
//                // if the set of coordinates isn't within 3 spaces of p1 or p2 add it to temp arrayList
////                System.out.println("Room: ");
////                System.out.println("Coords: " + x + " " + y);
////                System.out.println("Within p1: " + (withinThree(p1X, x) && withinThree(p1Y, y)));
//                if(!((withinThree(p1X, x) && withinThree(p1Y, y)) || ((withinThree(p2X, x) && withinThree(p2Y, y))))) {  // BUG HERE - probably needs to be an exclusive or rather than a regular or?
//                    int[] arr = {x, y};
//                    eligibleCoords.add(arr);
//                }
//            }
//        }
//
//        // gets random coordinate of all the eligible coordinates
//        Collections.shuffle(eligibleCoords);
//
//        return eligibleCoords.get(0);
//    }
//
//    public boolean withinThree(int pCoord, int curCoord) {
//        int distance = curCoord - pCoord;
//        if(distance >= 3 || distance <= -3 ) {
//            return false;
//        }
//        return true;
//
//    }

}
