package com.silentgames.silent_planet.model.cells.onVisible.Crystals;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by Lantiets on 29.08.2017.
 */

public class Two extends Crystals {
    public Two() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.two_crystals));
        super.setCanMove(true);
        super.setCrystals(2);
    }
}
