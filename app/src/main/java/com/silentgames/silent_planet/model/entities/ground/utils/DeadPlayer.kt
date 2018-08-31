package com.silentgames.silent_planet.model.entities.ground.utils

import android.graphics.Bitmap
import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player

/**
 * Created by gidroshvandel on 24.09.16.
 */
class DeadPlayer(
        player: Player,
        override var bitmap: Bitmap = player.bitmap,
        override var isDead: Boolean = true,
        override var isCanMove: Boolean = false
) : Player(
        "${player.playerName} ${App.getContext().resources.getString(R.string.deadPlayer)}",
        player.fraction)