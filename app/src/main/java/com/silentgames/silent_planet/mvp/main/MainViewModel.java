package com.silentgames.silent_planet.mvp.main;

import com.silentgames.silent_planet.model.Cell;

import java.util.Map;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class MainViewModel {

    private Cell[][] gameMatrix;

    private Map<String,String> oldXY;

    public Map<String, String> getOldXY() {
        return oldXY;
    }

    public void setOldXY(Map<String, String> oldXY) {
        this.oldXY = oldXY;
    }

    public Cell[][] getGameMatrix() {
        return gameMatrix;
    }

    public void setGameMatrix(Cell[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }
}
