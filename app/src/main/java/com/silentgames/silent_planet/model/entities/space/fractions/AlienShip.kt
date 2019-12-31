package com.silentgames.silent_planet.model.entities.space.fractions

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class AlienShip(
        context: Context,
        override var bitmap: Bitmap = BitmapEditor.getEntityBitmap(context, R.drawable.aliens_space_ship),
        override var name: String = context.getString(R.string.alien_ship_name),
        override var description: String = context.getString(R.string.alien_ship_description)
) : SpaceShip(context, Aliens) {
    override fun copy(): EntityType = AlienShip(
            context
    ).also {
        it.playersOnBord = playersOnBord.toMutableList()
        it.crystals = crystals
        it.isCanFly = isCanFly
        it.isCanMove = isCanMove
        it.isDead = isDead
        it.effects = effects
        it.goal = goal
    }
}
