package com.silentgames.silent_planet.model.entities.ground.fractions

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class Pirate(playerName: String) : Player() {
    init {
        super.bitmap = BitmapEditor.getEntityBitmap(R.drawable.pirate)
        super.playerName = playerName
        super.isCanMove = true
        super.fraction = Pirates.getInstance()
    }
}
