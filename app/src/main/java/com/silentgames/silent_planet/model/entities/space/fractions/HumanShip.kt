package com.silentgames.silent_planet.model.entities.space.fractions

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class HumanShip(
        override var bitmap: Bitmap = BitmapEditor.getEntityBitmap(R.drawable.human_space_ship),
        override var name: String = "Пионер",
        override var description: String = "Космический корабль землян"
) : SpaceShip(Humans.getInstance())
