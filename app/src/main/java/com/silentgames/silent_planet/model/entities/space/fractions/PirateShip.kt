package com.silentgames.silent_planet.model.entities.space.fractions

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class PirateShip : SpaceShip() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.pirate_space_ship)
        super.isCanFly = true
        super.fraction = Pirates.getInstance()
    }
}
