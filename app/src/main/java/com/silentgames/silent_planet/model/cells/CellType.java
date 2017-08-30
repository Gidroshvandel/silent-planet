package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.CellEx;
import com.silentgames.silent_planet.model.cells.defaultCell.Default;
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class CellType extends CellTypeEx  {

    private Default aDefault;
    private OnVisible onVisible;

    public CellType(OnVisible onVisible) {
        this.onVisible = onVisible;
        setAll(onVisible);
    }

    public CellType(Default aDefault) {
        this.aDefault = aDefault;
        setAll(aDefault);
    }

    public OnVisible getOnVisible() {
        return onVisible;
    }

    public void setOnVisible(OnVisible onVisible) {
        this.onVisible = onVisible;
    }

    public Default getDefault() {
        return aDefault;
    }

    public void setDefault(Default aDefault) {
        this.aDefault = aDefault;
    }

}
