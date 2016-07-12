package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.DefaultClass;

/**
 * Created by gidroshvandel on 13.07.16.
 */
public class OnVisible extends DefaultClass {

    EmptyCell emptyCell;

    public OnVisible(EmptyCell emptyCell) {
        this.emptyCell = emptyCell;
        setBitmap(emptyCell.getBitmap());
        setCanFly(emptyCell.isCanFly());
        setCanMove(emptyCell.isCanMove());
    }

    public EmptyCell getEmptyCell() {
        return emptyCell;
    }

    public void setEmptyCell(EmptyCell emptyCell) {
        this.emptyCell = emptyCell;
    }
}
