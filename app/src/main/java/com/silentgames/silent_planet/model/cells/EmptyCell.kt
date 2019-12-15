package com.silentgames.silent_planet.model.cells

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 13.07.16.
 */
class EmptyCell(
        context: Context,
        override val position: Axis,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.empty_cell),
        override var isCanMove: Boolean = true
) : CellType(
        context,
        name = context.getString(R.string.empty_cell_name),
        description = context.getString(R.string.empty_cell_description)) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
