package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.07.16.
 */
abstract class CellType(
        var crystals: Int = 0,
        override var closeBitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.planet_background),
        override var isCanFly: Boolean = false,
        override var isDead: Boolean = false,
        override var isVisible: Boolean = false
) : CellTypeEx {

    abstract fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper

    fun getVisibleBitmap(): Bitmap = if (isVisible) {
        bitmap
    } else {
        closeBitmap
    }
}
