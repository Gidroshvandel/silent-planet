package com.silentgames.silent_planet.model.entities.space.fractions

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class RobotShip(
        context: Context,
        override var bitmap: Bitmap = BitmapEditor.getEntityBitmap(context, R.drawable.robot_space_ship),
        override var name: String = "Зиро",
        override var description: String = "Космический корабль роботов"
) : SpaceShip(context, Robots.getInstance())
