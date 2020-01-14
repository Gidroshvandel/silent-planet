package com.silentgames.core.logic.ecs.component

import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class Transport(position: Position, unitsOnBoard: List<UnitEcs> = listOf()) : ComponentEquals() {

    val unitsOnBoard: List<UnitEcs> get() = mutableUnitsOnBoard.toList()
    private val mutableUnitsOnBoard: MutableList<UnitEcs> = unitsOnBoard.toMutableList()

    init {
        position.onPositionChanged = { axis ->
            this.unitsOnBoard.forEach {
                it.getComponent<Position>()?.currentPosition = axis
            }
        }
    }

    fun addOnBoard(unit: UnitEcs) {
        mutableUnitsOnBoard.add(unit)
    }

    fun removeFromBoard(unit: UnitEcs) {
        mutableUnitsOnBoard.remove(unit)
    }

}