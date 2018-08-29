package com.silentgames.silent_planet.model.entities.space.fractions

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class AlienShip : SpaceShip() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.aliens_space_ship)
        super.isCanFly = true
        super.fraction = Aliens.getInstance()
    }
}
