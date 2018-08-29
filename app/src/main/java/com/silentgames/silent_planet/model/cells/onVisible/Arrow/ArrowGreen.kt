package com.silentgames.silent_planet.model.cells.onVisible.Arrow

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
class ArrowGreen : Arrow() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell)
        super.isCanMove = true
        super.rotateAngle = BitmapEditor.RotateAngle.DEGREES0
        super.distance = 1
    }

}
