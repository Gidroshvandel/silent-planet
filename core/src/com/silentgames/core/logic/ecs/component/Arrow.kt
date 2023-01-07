package com.silentgames.core.logic.ecs.component

import java.util.*

class Arrow(
    val distance: Int,
    val arrowMode: ArrowMode,
    val rotateAngle: RotateAngle
) : ComponentEquals()

enum class ArrowMode {
    DIRECT,
    SLANTING
}

enum class RotateAngle {
    DEGREES0, DEGREES90, DEGREES180, DEGREES270;

    companion object {
        private val VALUES = values().toList()
        private val SIZE = VALUES.size
        private val RANDOM = Random()
        fun randomAngle(): RotateAngle {
            return VALUES[RANDOM.nextInt(SIZE)]
        }
    }
}
