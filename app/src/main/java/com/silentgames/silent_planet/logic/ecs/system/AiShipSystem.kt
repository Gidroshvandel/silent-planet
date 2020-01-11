package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.ArtificialIntelligence
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.TargetPosition
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType

class AiShipSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        if (!unit.hasComponent<TargetPosition>() && unit.hasComponent<ArtificialIntelligence>()) {
            unit.getComponent<FractionsType>()?.let { fractionsType ->
                val capitalShip = gameState.getCapitalShip(fractionsType)
                capitalShip?.getMoveShipPosition(gameState)?.let {
                    capitalShip.addComponent(TargetPosition(it))
                }
            }
        }
    }

    fun Unit.getMoveShipPosition(gameState: GameState): Axis? {
        val shipPosition = getCurrentPosition() ?: return null
        val cellsAtMoveDistance = gameState.getCellsAtMoveDistance(shipPosition).getCanMoveCells(this)
        return if (cellsAtMoveDistance.isNotEmpty()) {
            cellsAtMoveDistance.random().getCurrentPosition()
        } else {
            null
        }
    }

    private fun List<Cell>.getCanMoveCells(unit: Unit) = filter {
        unit.getComponent<MovingMode>() == it.getComponent<MovingMode>()
    }


}