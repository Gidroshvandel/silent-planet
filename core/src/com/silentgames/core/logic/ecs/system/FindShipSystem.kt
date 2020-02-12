package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class FindShipSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            if (cell.hasComponent<Crystal>() && unit.hasComponent<ArtificialIntelligence>() && unit.isPlayer()) {
                this.findShip(gameState, unit, cell)
            }
        }
    }

    private fun findShip(gameState: GameState, unit: UnitEcs, cell: CellEcs) {
        when {
            cell.getCrystalsCount() > 0 -> {
                gameState.addEvent(AddCrystalEvent(cell.getCrystalsCount(), unit))
                gameState.getSpaceShipPosition(unit)?.let {
                    unit.addComponent(Goal(it))
                }
            }
            unit.getCrystalsCount() > 0 -> {
                gameState.getSpaceShipPosition(unit)?.let {
                    unit.addComponent(Goal(it))
                }
            }
        }
    }

    private fun GameState.getSpaceShipPosition(unit: UnitEcs): Axis? {
        val unitFractionsType = unit.getComponent<FractionsType>() ?: return null
        return getCapitalShipPosition(unitFractionsType)?.currentPosition ?: return null
    }

    private fun UnitEcs.isPlayer(): Boolean =
            this.getComponent<MovingMode>() == MovingMode.WALK
}