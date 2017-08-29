package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.CellEx;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class CellTypeEx extends CellEx {

    public void setAll(CellTypeEx cellEx){
        setBitmap(cellEx.getBitmap());
        setCanFly(cellEx.isCanFly());
        setCanMove(cellEx.isCanMove());
        setDead(cellEx.isDead());
        setCrystals(cellEx.getCrystals());
    }

}
