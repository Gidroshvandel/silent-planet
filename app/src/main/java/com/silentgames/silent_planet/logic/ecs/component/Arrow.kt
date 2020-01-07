package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.utils.BitmapEditor

class Arrow(
        val distance: Int,
        val arrowMode: ArrowMode,
        val rotateAngle: BitmapEditor.RotateAngle
) : ComponentEquals()

enum class ArrowMode {
    DIRECT,
    SLANTING
}