package com.silentgames.core.logic.ecs.component

class TurnMode(val groupType: GroupType) : ComponentEquals()

enum class GroupType {
    SHIP,
    PLAYER
}