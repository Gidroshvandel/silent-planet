package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Crystal
import com.silentgames.silent_planet.logic.ecs.component.Hide
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.entity.EntityEcs
import com.silentgames.silent_planet.logic.ecs.entity.cell.CellEcs
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs

fun GameState.getCurrentUnitCell(unit: UnitEcs, cellExist: (cell: CellEcs, position: Axis) -> Unit) {
    val position = unit.getComponent<Position>()?.currentPosition
    if (position != null) {
        getCell(position)?.let { cellExist.invoke(it, position) }
    }
}

fun GameState.getCurrentUnitCell(unit: UnitEcs, cellExist: (cell: CellEcs) -> Unit) {
    val position = unit.getComponent<Position>()?.currentPosition
    if (position != null) {
        getCell(position)?.let { cellExist.invoke(it) }
    }
}

fun CellEcs.isVisible() = !isHide()

fun CellEcs.isHide() = hasComponent<Hide>()

fun EntityEcs.getCrystalsCount() = getComponent<Crystal>()?.count ?: 0

fun EntityEcs.getCurrentPosition() = getComponent<Position>()?.currentPosition

fun GameState.getCellsAtMoveDistance(position: Axis) =
        getAvailableMoveDistancePositionList(position).mapNotNull { this.getCell(it) }

fun getAvailableMoveDistancePositionList(position: Axis): List<Axis> {
    val positionList = mutableListOf<Axis>()
    for (i in 0..2) {
        for (j in 0..2) {
            val x = position.x + j - 1
            val y = position.y + i - 1
            if ((x != position.x
                            || y != position.y)
                    && x >= 0
                    && y >= 0
                    && x < Constants.horizontalCountOfCells
                    && y < Constants.verticalCountOfCells) {
                positionList.add(Axis(x, y))
            }
        }
    }
    return positionList
}