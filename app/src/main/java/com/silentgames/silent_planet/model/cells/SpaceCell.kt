package com.silentgames.silent_planet.model.cells

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 07.07.16.
 */
class SpaceCell(
        context: Context,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.space_texture),
        override var isCanMove: Boolean = false
) : CellType(
        context,
        isCanFly = true,
        closeBitmap = BitmapEditor.getCellBitmap(context, R.drawable.space_texture),
        name = context.getString(R.string.space_cell_name),
        description = context.getString(R.string.space_cell_description)
) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
