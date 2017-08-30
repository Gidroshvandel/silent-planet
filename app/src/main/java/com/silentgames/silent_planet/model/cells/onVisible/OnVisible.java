package com.silentgames.silent_planet.model.cells.onVisible;

import android.graphics.Bitmap;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.CellEx;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.cells.CellTypeEx;
import com.silentgames.silent_planet.model.cells.defaultCell.Default;

/**
 * Created by gidroshvandel on 13.07.16.
 */
public abstract class OnVisible extends CellTypeEx {

    private int crystals = 0;

    public int getCrystals() {
        return crystals;
    }

    public void setCrystals(int crystals) {
        this.crystals = crystals;
    }

    public abstract GameMatrixHelper doEvent(GameMatrixHelper gameMatrixHelper);

}
