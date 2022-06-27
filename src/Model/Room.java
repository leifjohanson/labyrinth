package Model;

import javax.swing.*;
import java.util.HashMap;

public class Room {
    boolean isTreasureRoom = false;
    public boolean containsTreasure = false;
    boolean isP1SecretRoom = false;
    boolean isP2SecretRoom = false;

    HashMap<String, Integer> coords = new HashMap<>();

    public boolean northWall = true;
    public boolean northWallVisible = false;
    public int northWallWidth = 0;

    public void setNorthWall(boolean north) {
        this.northWall = north;
    }

    public void setNorthWallVisible(boolean visible) {
        this.northWallVisible = visible;
    }

    public boolean southWall = true;
    public boolean southWallVisible = false;
    public int southWallWidth = 0;

    public void setSouthWall(boolean south) {
        this.southWall = south;
    }

    public void setSouthWallVisible(boolean visible) {
        this.southWallVisible = visible;
    }

    public boolean westWall = true;
    public boolean westWallVisible = false;
    public int westWallWidth = 0;

    public void setWestWall(boolean west) {
        this.westWall = west;
    }

    public void setWestWallVisible(boolean visible) {
        this.westWallVisible = visible;
    }

    public boolean eastWall = true;
    public boolean eastWallVisible = false;

    public void setEastWall(boolean east) {
        this.eastWall = east;
    }

    public void setEastWallVisible(boolean visible) {
        this.eastWallVisible = visible;
    }

    public Room () {

    }
    public Room(int x, int y) {
        coords.put("x", x);
        coords.put("y", y);
    }

    public boolean equalPlace(Room otherRoom) {
        if((this.getCoords().get("x") == otherRoom.getCoords().get("x")) && (this.getCoords().get("y") == otherRoom.getCoords().get("y"))) {
            return true;
        }
        return false;
    }


    public HashMap<String, Integer> getCoords() {
        return this.coords;
    }

    public void setTreasureRoom(boolean isTreasureRoom) {
        this.isTreasureRoom = isTreasureRoom;
    }

    public void setP1SecretRoom(boolean isP1SecretRoom) {
        this.isP1SecretRoom = isP1SecretRoom;
    }

    public void setP2SecretRoom(boolean isP2SecretRoom) {
        this.isP2SecretRoom = isP2SecretRoom;
    }

    public boolean isP1SecretRoom() {
        return this.isP1SecretRoom;
    }

    public boolean isP2SecretRoom() {
        return this.isP2SecretRoom;
    }

    public boolean isTreasureRoom() {
        return this.isTreasureRoom;
    }
}
