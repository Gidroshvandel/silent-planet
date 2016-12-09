package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.CellEx;
import com.silentgames.silent_planet.model.cells.defaultCell.DefaultCell;
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class CellType extends CellTypeEx {

    private DefaultCell defaultCell;
    private OnVisible onVisible;

    public CellType(OnVisible onVisible) {
        this.onVisible = onVisible;
        setAll(onVisible);
    }

    public CellType(DefaultCell defaultCell) {
        this.defaultCell = defaultCell;
        setAll(defaultCell);
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
