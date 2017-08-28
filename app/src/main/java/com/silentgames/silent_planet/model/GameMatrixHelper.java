package com.silentgames.silent_planet.model;

import java.util.Map;

/**
 * Created by gidroshvandel on 28.06.17.
 */
public class GameMatrixHelper {

    private Cell[][] gameMatrix;

    private int x;

    private int y;

    private Map<String,Integer> oldXY;

    private String playerName;

    private boolean eventMove;

    public GameMatrixHelper() {
    }

    public GameMatrixHelper(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GameMatrixHelper(int x, int y, Map<String, Integer> oldXY) {
        this.x = x;
        this.y = y;
        this.oldXY = oldXY;
    }

    public boolean isEventMove() {
        return eventMove;
    }

    public void setEventMove(boolean eventMove) {
        this.eventMove = eventMove;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map<String, Integer> getOldXY() {
        return oldXY;
    }

    public void setOldXY(Map<String, Integer> oldXY) {
        this.oldXY = oldXY;
    }

    public Cell[][] getGameMatrix() {
        return gameMatrix;
    }

    public void setGameMatrix(Cell[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    public void setGameMatrixCellByXY(Cell gameMatrixCell) {
        this.gameMatrix[x][y] = gameMatrixCell;
    }

    public Cell getGameMatrixCellByOldXY() {
        return gameMatrix[oldXY.get("X")][oldXY.get("Y")];
    }

    public void setGameMatrixCellByOldXY(Cell gameMatrixCell) {
        this.gameMatrix[oldXY.get("X")][oldXY.get("Y")] = gameMatrixCell;
    }

    public Cell getGameMatrixCellByXY() {
        return gameMatrix[x][y];
    }


    public Cell getGameMatrixCell(int x, int y) {
        return gameMatrix[x][y];
    }

    public void setGameMatrixCell(int x, int y, Cell gameMatrixCell) {
        this.gameMatrix[x][y] = gameMatrixCell;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
