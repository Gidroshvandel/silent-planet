package com.silentgames.silent_planet.model.cells.onVisible.Crystals;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.cells.CellTypeEx;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by Lantiets on 29.08.2017.
 */

public class One extends Crystals{
    public One() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.one_crystal));
        super.setCanMove(true);
        super.setCrystals(1);
    }
}
