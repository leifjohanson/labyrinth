package Controller;

import Model.*;

import java.util.ArrayList;
import java.util.Collections;

public class MapViewController {

    public GameManager game = new GameManager();
    public Room roomPressed = new Room();
    public boolean nextClicked = false;

    private boolean p1Picked = false;
    private boolean p1Confirmed = false;

    private boolean p1EliminatedAnnounced = false;
    private boolean p2EliminatedAnnounced = false;

    private boolean p2Prompted = false;

    public String labelMessage = "";

    public boolean wallHit = false;

    public void setRoomPressed(Room roomPressed) {
        this.roomPressed = roomPressed;
    }

    public void setGame(GameManager game) {
        this.game = game;
    }

    public void buttonClicked() {

        System.out.println("\nPlayer 1 Alive: " + !game.p1.eliminated + "\nPlayer 2 Alive: " + !game.p2.eliminated);

        if(!game.started) {
            if(!p1Confirmed) { // if p1 hasn't been confirmed, selected room is picked
                labelMessage = "Choose a different room or press next to confirm!";
                game.p1.setSecretRoom(roomPressed);
                game.p1.eliminated = false;
                p1Picked = true;
            } else { // if p1 has been picked
                if(roomPressed == game.p1.secretRoom) {
                    labelMessage = "You cannot have the same secret room as Player 1!";
                } else {
                    game.p2.setSecretRoom(roomPressed);
                    game.p2.eliminated = false;
                }
            }
        } else {
        // p1s turn
            if(game.turn == 0) {
                wallHit = false;
                String direction = determineDirection(game.p1.curRoom, roomPressed);

                switch (direction) {
                    case "n":
                        System.out.println("N is being reached");
                        if(game.p1.curRoom.northWall && !game.p1.curRoom.northWallVisible) {
                            System.out.println("It thinks n has an invisible north wall");
                            labelMessage = "An invisible wall has been hit. Player 1, your turn is over!";
                            System.out.println(labelMessage);
                            game.p1.curRoom.setNorthWallVisible(true);
                            roomPressed.setSouthWallVisible(true);
                            game.turn = determineTurn(game.p1);
                            endTurn(game.p1);
                            wallHit = true;
                            return;
                        } else if (game.p1.curRoom.northWall) {
                            System.out.println("It thinks n has a visible north wall");
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    case "e":
                        if(game.p1.curRoom.eastWall && !game.p1.curRoom.eastWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 1, your turn is over!";
                            game.p1.curRoom.setEastWallVisible(true);
                            roomPressed.setWestWallVisible(true);
                            game.turn = determineTurn(game.p1);
                            endTurn(game.p1);
                            wallHit = true;
                            return;
                        } else if (game.p1.curRoom.eastWall) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    case "s":
                        if(game.p1.curRoom.southWall && !game.p1.curRoom.southWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 1, your turn is over!";
                            game.p1.curRoom.setSouthWallVisible(true);
                            roomPressed.setNorthWallVisible(true);
                            game.turn = determineTurn(game.p1);
                            endTurn(game.p1);
                            wallHit = true;
                            return;
                        } else if (game.p1.curRoom.southWall) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    case "w":
                        if(game.p1.curRoom.westWall && !game.p1.curRoom.westWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 1, your turn is over!";
                            game.p1.curRoom.setWestWallVisible(true);
                            roomPressed.setEastWallVisible(true);
                            game.turn = determineTurn(game.p1);
                            endTurn(game.p1);
                            wallHit = true;
                            return;
                        } else if (game.p1.curRoom.westWall && game.p1.curRoom.westWallVisible) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    default:
                        wallHit = false;
                        break;

                }
                System.out.println("Cur Room within 1: " + within1(game.p1.getCurRoom(), roomPressed) +
                        "\nWall hit? " + wallHit);
                if ((within1(game.p1.getCurRoom(), roomPressed)) && !wallHit) {
                    game.p1.curRoom = roomPressed;
                    game.p1.moveCount++;
                    if(game.p1.hasTreasure) {
                        game.treasure.moveTo(roomPressed);
                    }

                    // if moved to foe room
                    if (((game.p1.curRoom.getCoords().get("x") == game.foe.curRoom.getCoords().get("x")) && game.p1.curRoom.getCoords().get("y") == game.foe.curRoom.getCoords().get("y"))) {
                        if(game.p1.hasTreasure) {
                            game.treasure.moveTo(game.treasure.treasureRoom);
                        }
                        game.p1.playerHit();
                        game.foe.isVisible = true;
                        labelMessage = "Player 1 has been attacked!";
                        game.turn = determineTurn(game.p1);
                        checkIfEliminated();
                        endTurn(game.p1);

                        // if moved into treasure room
                    } else if ((game.p1.curRoom.isTreasureRoom()) && game.treasure.curRoom.equalPlace(game.p1.curRoom)) {
                        game.p1.getsTreasure();
                        game.treasure.isVisible = true;
                        labelMessage = "Player 1 has the Treasure!";
                        game.turn = determineTurn(game.p1);
                        endTurn(game.p1);
                    }


                    if(game.p1.curRoom.equalPlace(game.p1.secretRoom) && game.p1.hasTreasure) {
                        labelMessage = "Player 1 Wins!";
                        game.gameOver = true;
                        return;
                    }

                    // if moved into other players room with treasure
                    if(!game.p2.eliminated) {
                        if (((game.p1.curRoom.getCoords().get("x") == game.p2.curRoom.getCoords().get("x")) && game.p1.curRoom.getCoords().get("y") == game.p2.curRoom.getCoords().get("y")) && game.p2.hasTreasure) {
                            if(game.p1.actualFatigue >= game.p2.actualFatigue) {
                                game.p1.getsTreasure();
                                moveAway(game.p2);
                                game.p2.losesTreasure();
                                labelMessage = "P1 Took P2's Treasure!";
                                game.turn = determineTurn(game.p1);
                                endTurn(game.p1);
                            } else {
                                moveAway(game.p1);
                                labelMessage = "P1 Tried to Take P2's Treasure and Failed!";
                                game.turn = determineTurn(game.p1);
                                endTurn(game.p1);
                            }
                        }
                    }
                    // if player has hit the maximum moves
                    if ((game.p1.moveCount >= game.p1.fatigue)) {
                        labelMessage = "Player 1 has made maximum moves!";
                        game.turn = determineTurn(game.p1);
                        endTurn(game.p1);
                    }


                } else {
                    labelMessage = "Room not valid, try again!";
                }


                // p2s turn
            } else {
                wallHit = false;
                String direction = determineDirection(game.p2.curRoom, roomPressed);

                switch (direction) {
                    case "n":
                        if(game.p2.curRoom.northWall && !game.p2.curRoom.northWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 2, your turn is over!";
                            game.p2.curRoom.setNorthWallVisible(true);
                            roomPressed.setSouthWallVisible(true);
                            game.turn = determineTurn(game.p2);
                            endTurn(game.p2);
                            wallHit = true;
                            return;
                        } else if (game.p2.curRoom.northWall && game.p2.curRoom.northWallVisible) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    case "e":
                        if(game.p2.curRoom.eastWall && !game.p2.curRoom.eastWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 2, your turn is over!";
                            game.p2.curRoom.setEastWallVisible(true);
                            roomPressed.setWestWallVisible(true);
                            game.turn = determineTurn(game.p2);
                            endTurn(game.p2);
                            wallHit = true;
                            return;
                        } else if (game.p2.curRoom.eastWall && game.p2.curRoom.eastWallVisible) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    case "s":
                        if(game.p2.curRoom.southWall && !game.p2.curRoom.southWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 2, your turn is over!";
                            game.p2.curRoom.setSouthWallVisible(true);
                            roomPressed.setNorthWallVisible(true);
                            game.turn = determineTurn(game.p2);
                            endTurn(game.p2);
                            wallHit = true;
                            return;
                        } else if (game.p2.curRoom.southWall && game.p2.curRoom.southWallVisible) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                    case "w":
                        if(game.p2.curRoom.westWall && !game.p2.curRoom.westWallVisible) {
                            labelMessage = "An invisible wall has been hit. Player 2, your turn is over!";
                            game.p2.curRoom.setWestWallVisible(true);
                            roomPressed.setEastWallVisible(true);
                            game.turn = determineTurn(game.p2);
                            endTurn(game.p2);
                            wallHit = true;
                            return;
                        } else if (game.p2.curRoom.westWall && game.p2.curRoom.westWallVisible) {
                            labelMessage = "You hit a visible wall, try a different move!";
                            wallHit = true;
                            return;
                        } else {
                            wallHit = false;
                        }
                        break;
                }

                if ((within1(game.p2.getCurRoom(), roomPressed)) && !wallHit) {

                    game.p2.moveTo(roomPressed);
                    game.p2.moveCount++;

                    if (game.p2.hasTreasure) {
                        game.treasure.moveTo(roomPressed);
                    }
                    // if moved to foe room
                    if ((game.p2.curRoom.getCoords().get("x") == game.foe.curRoom.getCoords().get("x")) && game.p2.curRoom.getCoords().get("y") == game.foe.curRoom.getCoords().get("y")) {
                        if (game.p2.hasTreasure) {
                            game.treasure.moveTo(game.treasure.treasureRoom);
                        }
                        game.p2.playerHit();
                        game.foe.isVisible = true;
                        labelMessage = "Player 2 has been attacked!";
                        game.turn = determineTurn(game.p2);
                        checkIfEliminated();
                        endTurn(game.p2);
                        // if moved into other players room with treasure
                    } else if (((game.p2.curRoom.getCoords().get("x") == game.p1.curRoom.getCoords().get("x")) && game.p2.curRoom.getCoords().get("y") == game.p1.curRoom.getCoords().get("y")) && game.p1.hasTreasure) {
                        if (game.p2.actualFatigue >= game.p1.actualFatigue) {
                            game.p2.getsTreasure();
                            moveAway(game.p1);
                            game.p1.losesTreasure();
                            labelMessage = "P2 Took P1's Treasure!";
                            game.turn = determineTurn(game.p2);
                            endTurn(game.p2);
                        } else {
                            moveAway(game.p2);
                            labelMessage = "P2 Tried to Take P1's Treasure and Failed!";
                            game.turn = determineTurn(game.p2);
                            endTurn(game.p2);
                        }
                        // if in treasure room with treasure
                    } else if ((game.p2.curRoom.isTreasureRoom()) && game.treasure.curRoom == game.p2.curRoom) {
                        game.p2.getsTreasure();
                        game.treasure.isVisible = true;
                        labelMessage = "Player 2 has the Treasure!";
                        game.turn = determineTurn(game.p2);
                        endTurn(game.p2);
                    }

                    // winning scenario
                    if(game.p2.curRoom.equalPlace(game.p2.secretRoom) && game.p2.hasTreasure) {
                        labelMessage = "Player 2 Wins!";
                        game.gameOver = true;
                        return;
                    }

                    if ((game.p2.moveCount >= game.p2.fatigue)) {
                        labelMessage = "Player 2 has made maximum moves!";
                        game.turn = determineTurn(game.p2);
                        endTurn(game.p2);
                    }

                } else {
                    labelMessage = "Room not valid, try again!";
                }
            }
        }
    }

    // executes when next button is clicked
    public void nextButtonClicked() {
        if(!game.started) {
            if(!p1Picked) { // player 1 hits next without picking room
                labelMessage = "Player 1, pick your secret room!";
            } else if (!p2Prompted) { // player 1 hits next to confirm, player 2 is prompted
                p1Confirmed = true;
                labelMessage = "Player 2, if playing, pick your secret room!";
                p2Prompted = true;
            } else {
                labelMessage = "Game has started!";
                generateLabyrinth();
                game.started = true;
            }

        } else {
            nextClicked = true;
            if (game.turn == 0) {
                game.turn = determineTurn(game.p1);
                endTurn(game.p1);
            } else {
                game.turn = determineTurn(game.p2);
                endTurn(game.p2);
            }
        }
    }

    public int determineTurn(Player curPlayer) {
        if(curPlayer.name.equals("p1")) {
            if(game.livePlayerCount() == 2) {
                labelMessage = "Player 2, you're up!";
                return 1;
            } else {
                labelMessage = "Player 1, you're up!";
                return 0;
            }
        } else if(curPlayer.name.equals("p2")) {
            if(game.livePlayerCount() == 2) {
                labelMessage = "Player 1, you're up!";
                return 0;
            } else {
                labelMessage = "Player 2, you're up!";
                return 1;
            }
        }

        System.out.println("There is an error in determineTurn()");
        return -1;

    }

    public void endTurn(Player curPlayer) {
        curPlayer.moveCount = 0;
        game.turnPerRound ++;

        if(curPlayer == game.p1) {
            if(game.p1.curRoom == game.p2.curRoom) {
                move1Away(game.p1);
            }
        } else if(curPlayer == game.p2) {
            if(game.p1.curRoom == game.p2.curRoom) {
                move1Away(game.p2);
            }
        }

        if(game.turnPerRound == game.livePlayerCount()) {
            endRound();
        }
    }

    public void endRound() {
        game.turnPerRound = 0;
        if (game.foe.scanForPlayers(game.p1, game.p2) == null) {
            labelMessage = "The foe is sound asleep!";
        } else if (game.foe.scanForPlayers(game.p1, game.p2) == game.p1) {
            labelMessage = "The foe chases after player 1!";
            game.foe.moveTo(findFoeRoom(game.p1.curRoom));
        } else if (game.foe.scanForPlayers(game.p1, game.p2) == game.p2) {
            labelMessage = "The foe chases after player 2!";
            game.foe.moveTo(findFoeRoom(game.p2.curRoom));
        }


        // foe moves and lands on p1
        if(game.foe.curRoom == game.p1.curRoom) {
            if(game.p1.hasTreasure) {
                game.treasure.moveTo(game.treasure.treasureRoom);
            }
            game.p1.playerHit();
            game.foe.isVisible = true;
            checkIfEliminated();
            labelMessage = "Player 1 has been attacked!";
        }

        // foe moves and lands on p2
        else if (game.foe.curRoom == game.p2.curRoom) {
            if(game.p2.hasTreasure) {
                game.treasure.moveTo(game.treasure.treasureRoom);
            }
            game.p2.playerHit();
            game.foe.isVisible = true;
            checkIfEliminated();
            labelMessage = "Player 2 has been attacked!";
        }

        ArrayList<Boolean> randomRefill = new ArrayList<>();
        for(int i = 0; i < 8; i ++) {
            randomRefill.add(false);
        }
        randomRefill.add(true);
        Collections.shuffle(randomRefill);

        if(randomRefill.get(0)) {
            labelMessage = "All player's fatigues have been restored!";
            game.p1.actualFatigue = 8;
            game.p2.actualFatigue = 8;
        }

    }


    public boolean within1(Room curRoom, Room roomPressed) {
        boolean sameRoom = (curRoom.getCoords().get("x") == roomPressed.getCoords().get("x")) && (curRoom.getCoords().get("y") == roomPressed.getCoords().get("y"));
        boolean yWithin1 = (curRoom.getCoords().get("y") - roomPressed.getCoords().get("y") <= 1 && curRoom.getCoords().get("y") - roomPressed.getCoords().get("y") >= -1);
        boolean xWithin1 = (curRoom.getCoords().get("x") - roomPressed.getCoords().get("x") <= 1 && curRoom.getCoords().get("x") - roomPressed.getCoords().get("x") >= -1);
        boolean ySame = curRoom.getCoords().get("y") - roomPressed.getCoords().get("y") == 0;
        boolean xSame = curRoom.getCoords().get("x") - roomPressed.getCoords().get("x") == 0;
        boolean adjacent = (xSame && yWithin1) ^ (ySame && xWithin1);

        if(adjacent && !sameRoom) {
            return true;
        }
        return false;
    }

    // moves player 3 away
    public void moveAway(Player player) {
        int curX = player.curRoom.getCoords().get("x");
        int curY = player.curRoom.getCoords().get("y");

        ArrayList<int[]> eligibleCoords = new ArrayList<>();

        for(int y = curY - 3; y <= curY + 3; y ++) {
            for(int x = curX - 3; x <= curX + 3; x ++) {
                int[] arr = {x, y};
                boolean notNegative = (x >= 0 && x <= 8) && (y >= 0 && y <= 8);
                boolean threeAway = (x == curX - 3 || x == curX + 3) || (y == curY - 3 || y == curY + 3);
                if(notNegative && threeAway) {
                    eligibleCoords.add(arr);
                }
            }
        }

        Collections.shuffle(eligibleCoords);
        int[] randomCoord = eligibleCoords.get(0);

        while(game.labyrinth[randomCoord[0]][randomCoord[1]] == game.foe.curRoom) {
            Collections.shuffle(eligibleCoords);
            randomCoord = eligibleCoords.get(0);
        }

        player.moveTo(game.labyrinth[randomCoord[0]][randomCoord[1]]);

    }

    public void move1Away(Player player) {
        int curX = player.curRoom.getCoords().get("x");
        int curY = player.curRoom.getCoords().get("y");

        ArrayList<int[]> eligibleCoords = new ArrayList<>();

        for(int y = curY - 1; y <= curY + 1; y ++) {
            for(int x = curX - 1; x <= curX + 1; x ++) {
                int[] arr = {x, y};
                boolean notNegative = (x >= 0 && x <= 8) && (y >= 0 && y <= 8);
                boolean threeAway = (x == curX - 1 || x == curX + 1) || (y == curY - 1 || y == curY + 1);
                if(notNegative && threeAway) {
                    eligibleCoords.add(arr);
                }
            }
        }

        Collections.shuffle(eligibleCoords);
        int[] randomCoord = eligibleCoords.get(0);

        while(game.labyrinth[randomCoord[0]][randomCoord[1]] == game.foe.curRoom) {
            Collections.shuffle(eligibleCoords);
            randomCoord = eligibleCoords.get(0);
        }

        player.moveTo(game.labyrinth[randomCoord[0]][randomCoord[1]]);

    }

    public Room findFoeRoom(Room playerRoom) {
        int xFoeRoom = game.foe.curRoom.getCoords().get("x");
        int yFoeRoom = game.foe.curRoom.getCoords().get("y");

        int xPlayerRoom = playerRoom.getCoords().get("x");
        int yPlayerRoom = playerRoom.getCoords().get("y");


        if(xPlayerRoom < xFoeRoom && yPlayerRoom < yFoeRoom ) { // 1
            return game.labyrinth[xFoeRoom - 1][yFoeRoom - 1];
        } else if(xPlayerRoom == xFoeRoom && yPlayerRoom < yFoeRoom ) { // 2
            return game.labyrinth[xFoeRoom][yFoeRoom - 1];
        } else if(xPlayerRoom > xFoeRoom && yPlayerRoom < yFoeRoom ) { // 3
            return game.labyrinth[xFoeRoom + 1][yFoeRoom - 1];
        } else if(xPlayerRoom < xFoeRoom && yPlayerRoom == yFoeRoom ) { // 4
            return game.labyrinth[xFoeRoom - 1][yFoeRoom];
        } else if(xPlayerRoom > xFoeRoom && yPlayerRoom == yFoeRoom ) { // 5
            return game.labyrinth[xFoeRoom + 1][yFoeRoom];
        } else if(xPlayerRoom < xFoeRoom && yPlayerRoom > yFoeRoom ) { // 6
            return game.labyrinth[xFoeRoom - 1][yFoeRoom + 1];
        } else if(xPlayerRoom == xFoeRoom && yPlayerRoom > yFoeRoom ) { // 7
            return game.labyrinth[xFoeRoom][yFoeRoom + 1];
        } else if(xPlayerRoom > xFoeRoom && yPlayerRoom > yFoeRoom ) { // 8
            return game.labyrinth[xFoeRoom + 1][yFoeRoom + 1];
        } else {
            System.out.println("Error moving Foe");
        }

        return null;
    }

    public void generateLabyrinth() {

        int[] treasureCoords = pickTreasureRoom();

        for(int y = 0; y < 9; y++) {
            for(int x = 0; x < 9; x++) {
                if(x == treasureCoords[0] && y == treasureCoords[1]) { // if current room being looped through is the
                    game.labyrinth[x][y].setTreasureRoom(true);
                    game.treasure = new Treasure(game.labyrinth[x][y]);
                    game.foe = new Foe(game.labyrinth[x][y]);
                    game.labyrinth[x][y].containsTreasure = true;
                    //System.out.println("Treasure Room has been set at: " + Arrays.toString(treasureCoords) + " " + x + " " + y); // making sure its set
                } else if (game.p1.getSecretRoom().getCoords().get("x") == x && game.p1.getSecretRoom().getCoords().get("y") == y) {
                    game.labyrinth[x][y].setP1SecretRoom(true);
                    //System.out.println("Player 1 Secret Room has been set at: " + x + " " + y);
                } else if (!game.p2.isEliminated() && (game.p2.getSecretRoom().getCoords().get("x") == x && game.p2.getSecretRoom().getCoords().get("y") == y)) {
                    game.labyrinth[x][y].setP2SecretRoom(true);
                    //System.out.println("Player 2 Secret Room has been set at: " + x + " " + y);
                }
            }
        }

    }



    public int[] pickTreasureRoom() {
        int p1X = game.p1.getCurRoom().getCoords().get("x");
        int p1Y = game.p1.getCurRoom().getCoords().get("y");

        int p2X = 0;
        int p2Y = 0;
        // if p2 exists
        if(!game.p2.isEliminated()) {
            p2X = game.p2.getCurRoom().getCoords().get("x");
            p2Y = game.p2.getCurRoom().getCoords().get("y");
        } else { // p2 values are set to p1 values if p2 doesn't exist so it doesn't affect foe placement
            p2X = game.p1.getCurRoom().getCoords().get("x");
            p2Y = game.p1.getCurRoom().getCoords().get("y");
        }

        ArrayList<int[]> eligibleCoords = new ArrayList();

        for(int y = 0; y < 9; y ++) {
            for(int x = 0; x < 9; x++) {
                if(!((withinThree(p1X, x) && withinThree(p1Y, y)) || ((withinThree(p2X, x) && withinThree(p2Y, y))))) {
                    int[] arr = {x, y};
                    eligibleCoords.add(arr);
                }
            }
        }

        // gets random coordinate of all the eligible coordinates
        Collections.shuffle(eligibleCoords);

        return eligibleCoords.get(0);
    }

    public boolean withinThree(int pCoord, int curCoord) {
        int distance = curCoord - pCoord;
        if(distance > 3 || distance < -3 ) {
            return false;
        }
        return true;

    }

    public void checkIfEliminated() {
        if(!p1EliminatedAnnounced && game.p1.isEliminated()) {
            labelMessage = "Player 1 has been eliminated!";
            p1EliminatedAnnounced = true;
            game.turn = 1;
        }
        if(!p2EliminatedAnnounced && game.p2.isEliminated()) {
            labelMessage = "Player 2 has been eliminated!";
            p2EliminatedAnnounced = true;
            game.turn = 0;
        }
        if(game.p1.isEliminated() && game.p2.isEliminated()) {
            labelMessage = "All players have been eliminated! Game Over!";
            game.gameOver = true;
        }
    }


    public String determineDirection(Room curRoom, Room newRoom) {
        int xCurRoom = curRoom.getCoords().get("x");
        int yCurRoom = curRoom.getCoords().get("y");

        int xNewRoom = newRoom.getCoords().get("x");
        int yNewRoom = newRoom.getCoords().get("y");

        boolean north = (xNewRoom == xCurRoom) && (yNewRoom < yCurRoom);
        boolean west = (xNewRoom < xCurRoom) && (yNewRoom == yCurRoom);
        boolean east = (xNewRoom > xCurRoom) && (yNewRoom == yCurRoom);
        boolean south = (xNewRoom == xCurRoom) && (yNewRoom > yCurRoom);

        if(north) {
            return "n";
        } else if(west) {
            return "w";
        } else if(east) {
            return "e";
        } else if(south) {
            return "s";
        } else {
            System.out.println("Error determining direction");
            return "err";
        }

    }
}
