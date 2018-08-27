package com.silentgames.silent_planet.model.cells.onVisible.Crystals

import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible

/**
 * Created by Lantiets on 29.08.2017.
 */

abstract class Crystals : OnVisible() {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }
}
