package com.silentgames.silent_planet.model.entities.ground.utils

import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player

/**
 * Created by gidroshvandel on 24.09.16.
 */
class DeadPlayer(player: Player) : Player() {
    init {
        super.bitmap = player.bitmap
        super.playerName = player.playerName + " " + App.getContext().resources.getString(R.string.deadPlayer)
        super.isDead = true
        super.isCanMove = false
        super.fraction = player.fraction
    }
}
