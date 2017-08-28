package com.silentgames.silent_planet.model.cells.onVisible;

import android.graphics.Bitmap;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.CellEx;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.model.cells.CellTypeEx;

/**
 * Created by gidroshvandel on 13.07.16.
 */
public abstract class OnVisible extends CellTypeEx {

    public abstract GameMatrixHelper doEvent(GameMatrixHelper gameMatrixHelper);
}
