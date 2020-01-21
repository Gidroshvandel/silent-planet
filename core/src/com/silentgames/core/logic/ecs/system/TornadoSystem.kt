package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.component.Tornado
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.TornadoSystem.GameZone.*
import com.silentgames.core.utils.notNull

class TornadoSystem : UnitSystem() {
    private enum class GameZone(val axis: Axis) {
        UP_LEFT(Axis(1, 1)),
        UP_RIGHT(Axis(1, Constants.horizontalCountOfGroundCells)),
        BOTTOM_LEFT(Axis(Constants.verticalCountOfGroundCells, 1)),
        BOTTOM_RIGHT(Axis(Constants.verticalCountOfGroundCells, Constants.horizontalCountOfGroundCells))
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            cell.getComponent<Tornado>()?.let {
                notNull(gameState,
                        unit.getComponent<FractionsType>(),
                        unit.getCurrentPosition(),
                        unit,
                        ::tornado)
            }
        }
    }

    private fun tornado(gameState: GameState, fractionsType: FractionsType, position: Axis, unit: UnitEcs) {
        val axis = this.getTarget(gameState, fractionsType, position)
        if (axis != null) {
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(axis))
        }
    }

    fun getTarget(gameState: GameState, fractionsType: FractionsType, position: Axis): Axis? {
        val isAnglePosition = values().toList().map { it.axis }.contains(position)
        return if (isAnglePosition) gameState.getCapitalShip(fractionsType)?.getCurrentPosition() else this.contains(position)
    }

    private fun contains(position: Axis): Axis? {
        val middlePosition = Axis(
                Constants.horizontalCountOfGroundCells / 2,
                Constants.verticalCountOfGroundCells / 2
        )
        return when {
            position.x <= middlePosition.x && position.y <= middlePosition.y -> UP_LEFT.axis
            position.x <= middlePosition.x && position.y > middlePosition.y -> UP_RIGHT.axis
            position.x > middlePosition.x && position.y <= middlePosition.y -> BOTTOM_LEFT.axis
            position.x > middlePosition.x && position.y > middlePosition.y -> BOTTOM_RIGHT.axis

            else -> null
        }
    }
}