package com.silentgames.silent_planet.model.entities.ground.fractions

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class Human(playerName: String) : Player() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.human_spaceman)
        super.playerName = playerName
        super.isCanMove = true
        super.fraction = Humans.getInstance()
    }
}
