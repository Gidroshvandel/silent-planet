package com.silentgames.silent_planet.mvp.main;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.GameMatrixHelper;

import java.util.Map;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class MainViewModel {

    private GameMatrixHelper gameMatrixHelper;

    public GameMatrixHelper getGameMatrixHelper() {
        return gameMatrixHelper;
    }

    public void setGameMatrixHelper(GameMatrixHelper gameMatrixHelper) {
        this.gameMatrixHelper = gameMatrixHelper;
    }
}
