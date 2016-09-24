package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.model.cells.defaultCell.DefaultCell;
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class CellType extends DefaultClass {

    private DefaultCell defaultCell;
    private OnVisible onVisible;

    public CellType(OnVisible onVisible) {
        this.onVisible = onVisible;
        setBitmap(onVisible.getBitmap());
        setCanFly(onVisible.isCanFly());
        setCanMove(onVisible.isCanMove());
        setDead(onVisible.isDead());
    }

    public CellType(DefaultCell defaultCell) {
        this.defaultCell = defaultCell;
        setBitmap(defaultCell.getBitmap());
        setCanFly(defaultCell.isCanFly());
        setCanMove(defaultCell.isCanMove());
    }

    public OnVisible getOnVisible() {
        return onVisible;
    }

    public void setOnVisible(OnVisible onVisible) {
        this.onVisible = onVisible;
    }

    public DefaultCell getDefaultCell() {
        return defaultCell;
    }

    public void setDefaultCell(DefaultCell defaultCell) {
        this.defaultCell = defaultCell;
    }
}
