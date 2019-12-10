package com.silentgames.silent_planet.model.entities.ground.fractions

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class Robot(
        context: Context,
        override var name: String,
        override var bitmap: Bitmap = BitmapEditor.getEntityBitmap(context, R.drawable.robot),
        override var description: String = context.getString(R.string.robot_player_description)
) : Player(context, Robots)
