package com.silentgames.silent_planet.model.entities.ground.fractions

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class Robot(playerName: String) : Player() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.robot)
        super.playerName = playerName
        super.isCanMove = true
        super.fraction = Robots.getInstance()
    }
}
