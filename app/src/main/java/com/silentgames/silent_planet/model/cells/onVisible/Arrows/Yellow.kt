package com.silentgames.silent_planet.model.cells.onVisible.Arrows

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 28.08.2017.
 */

class Yellow : Arrow() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell)
        super.isCanMove = true
        super.rotateAngle = BitmapEditor.RotateAngle.DEGREES0
        super.distance = 2
    }
}
