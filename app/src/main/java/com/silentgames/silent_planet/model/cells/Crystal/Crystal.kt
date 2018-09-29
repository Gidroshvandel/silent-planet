package com.silentgames.silent_planet.model.cells.Crystal

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType

/**
 * Created by Lantiets on 29.08.2017.
 */

class Crystal(
        context: Context,
        titleType: CrystalsEnum,
        override var bitmap: Bitmap = titleType.getImage(context),
        override var isCanMove: Boolean = true
) : CellType(
        context,
        titleType.crystalsCount,
        name = context.getString(R.string.crystal_cell_name),
        description = context.getString(
                R.string.crystal_cell_description,
                titleType.crystalsCount
        )) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
