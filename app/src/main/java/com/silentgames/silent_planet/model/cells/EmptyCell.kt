package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 13.07.16.
 */
class EmptyCell(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.empty_cell),
        override var isCanMove: Boolean = true
) : CellType(
        name = App.getContext().getString(R.string.empty_cell_name),
        description = App.getContext().getString(R.string.empty_cell_description)) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
