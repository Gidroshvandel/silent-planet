package com.silentgames.core.logic.ecs.component

import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class Transport(
    @Transient private val mutableUnitsOnBoard: MutableList<UnitEcs> = mutableListOf()
) : ComponentEquals() {

    val unitsOnBoard: List<UnitEcs> get() = mutableUnitsOnBoard.toList()

    fun addOnBoard(unit: UnitEcs) {
        mutableUnitsOnBoard.add(unit)
    }

    fun removeFromBoard(unit: UnitEcs) {
        mutableUnitsOnBoard.remove(unit)
    }
}
