package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class Transport(position: Position, unitsOnBoard: List<Unit>) : ComponentEquals() {

    val unitsOnBoard: List<Unit> get() = mutableUnitsOnBoard.toList()
    private val mutableUnitsOnBoard: MutableList<Unit> = unitsOnBoard.toMutableList()

    init {
        position.onPositionChanged = { axis ->
            this.unitsOnBoard.forEach {
                it.getComponent<Position>()?.currentPosition = axis
            }
        }
    }

    fun addOnBoard(unit: Unit) {
        mutableUnitsOnBoard.add(unit)
    }

    fun removeFromBoard(unit: Unit) {
        mutableUnitsOnBoard.remove(unit)
    }

}