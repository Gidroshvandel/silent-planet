package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.07.16.
 */
abstract class CellType(
        override var crystals: Int = 0,
        override var closeBitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.planet_background),
        override var isCanFly: Boolean = false,
        override var isDead: Boolean = false,
        override var isVisible: Boolean = false,
        name: String = "",
        description: String = ""
) : CellTypeProperties {

    override var name: String = name
        get() = if (isVisible) field else App.getContext().getString(R.string.unknown_cell_name)

    override var description: String = description
        get() = if (isVisible) field else App.getContext().getString(R.string.unknown_cell_description)

    abstract fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper

    fun getCurrentBitmap(): Bitmap = if (isVisible) {
        bitmap
    } else {
        closeBitmap
    }
}
