package com.silentgames.core.logic.ecs

enum class MotionType {
    MOVEMENT, TELEPORT
}

class Motion(val axis: Axis, val motionType: MotionType)
