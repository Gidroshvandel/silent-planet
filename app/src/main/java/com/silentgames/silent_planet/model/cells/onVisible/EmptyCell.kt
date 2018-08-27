package com.silentgames.silent_planet.model.cells.onVisible

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 13.07.16.
 */
class EmptyCell : OnVisible() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.empty_cell)
        super.isCanMove = true
    }

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
