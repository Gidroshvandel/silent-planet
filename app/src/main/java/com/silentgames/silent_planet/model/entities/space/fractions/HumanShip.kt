package com.silentgames.silent_planet.model.entities.space.fractions

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class HumanShip : SpaceShip() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.human_space_ship)
        super.isCanFly = true
        super.fraction = Humans.getInstance()
    }
}
