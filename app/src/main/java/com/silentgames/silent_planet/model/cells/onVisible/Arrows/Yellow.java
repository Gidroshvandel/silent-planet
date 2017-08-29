package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by Lantiets on 28.08.2017.
 */

public class Yellow extends Arrow {

    public Yellow() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell));
        super.setCanMove(true);
        super.setRotateAngle(BitmapEditor.RotateAngle.DEGREES0);
        super.setDistance(2);
    }
}
