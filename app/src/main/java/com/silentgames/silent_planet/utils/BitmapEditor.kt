package com.silentgames.silent_planet.utils

import java.util.*

/**
 * Created by gidroshvandel on 07.07.16.
 */
object BitmapEditor {

    enum class RotateAngle {
        DEGREES0, DEGREES90, DEGREES180, DEGREES270;

        companion object {
            private val VALUES = Collections.unmodifiableList(Arrays.asList(*values()))
            private val SIZE = VALUES.size
            private val RANDOM = Random()
            fun randomAngle(): RotateAngle {
                return VALUES[RANDOM.nextInt(SIZE)]
            }
        }
    }
}