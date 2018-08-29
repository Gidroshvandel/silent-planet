package com.silentgames.silent_planet.model.cells.onVisible.Crystal

import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible

/**
 * Created by Lantiets on 29.08.2017.
 */

class Crystal(titleType: CrystalsEnum) : OnVisible() {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }

    init {
        super.bitmap = titleType.image
        super.isCanMove = true
        super.crystals = titleType.crystalsCount
    }
}
