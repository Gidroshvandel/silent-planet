package com.silentgames.silent_planet.model;

import java.util.Map;

/**
 * Created by gidroshvandel on 28.06.17.
 */
public class GameMatrixHelper {

     Cell[][] gameMatrix;

    private int x;

    private int y;

    private Map<String,String> oldXY;

    public GameMatrixHelper(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GameMatrixHelper(int x, int y, Map<String, String> oldXY) {
        this.x = x;
        this.y = y;
        this.oldXY = oldXY;
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

    public Map<String, String> getOldXY() {
        return oldXY;
    }

    public void setOldXY(Map<String, String> oldXY) {
        this.oldXY = oldXY;
    }
}
