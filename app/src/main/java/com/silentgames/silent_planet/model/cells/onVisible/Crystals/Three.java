package com.silentgames.silent_planet.model.cells.onVisible.Crystals;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by Lantiets on 29.08.2017.
 */

public class Three extends Crystals{
    public Three() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.three_crystals));
        super.setCanMove(true);
        super.setCrystals(3);
    }
}
