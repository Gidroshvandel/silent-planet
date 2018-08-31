package com.silentgames.silent_planet.model.cells.Crystal

import android.graphics.Bitmap
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType

/**
 * Created by Lantiets on 29.08.2017.
 */

class Crystal(
        titleType: CrystalsEnum,
        override var bitmap: Bitmap = titleType.image,
        override var isCanMove: Boolean = true
) : CellType(titleType.crystalsCount) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
