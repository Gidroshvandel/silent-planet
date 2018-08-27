package com.silentgames.silent_planet.model.entities.space.fractions

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class RobotShip : SpaceShip() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.robot_space_ship)
        super.isCanFly = true
        super.fraction = Robots.getInstance()
    }
}
