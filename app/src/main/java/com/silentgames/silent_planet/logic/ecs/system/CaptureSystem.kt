package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.notNull

class CaptureSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        notNull(
                unit.getComponent(),
                unit.getComponent(),
                unit,
                gameState,
                ::capture
        )
    }

    private fun capture(targetPosition: TargetPosition, unitFractionsType: FractionsType, unit: Unit, gameState: GameState) {
        val enemyUnits = gameState.getUnits(targetPosition.axis).filterNot {
            it.getComponent<FractionsType>() == unitFractionsType
                    || it.hasComponent<Transport>()
                    || it.hasComponent<CapitalShip>()
        }
        if (enemyUnits.size > 1) {
            gameState.captureUnitByEnemy(unit, enemyUnits)
        } else {
            enemyUnits.firstOrNull()?.let { enemy ->
                gameState.captureEnemyByUnit(enemy, unit, unitFractionsType)
            }
        }
    }

    private fun GameState.captureUnitByEnemy(unit: Unit, enemyUnits: List<Unit>) {
        enemyUnits.first().getComponent<FractionsType>()?.let { enemyFractionsType ->
            unit.addComponent(Capture(enemyFractionsType))
            getCapitalShipPosition(enemyFractionsType)?.currentPosition?.let { shipPosition ->
                unit.addComponent(Teleport())
                unit.addComponent(TargetPosition(shipPosition))
                enemyUnits.forEach {
                    it.addComponent(Teleport())
                    it.addComponent(TargetPosition(shipPosition))
                }
            }
        }
    }

    private fun GameState.captureEnemyByUnit(enemy: Unit, unit: Unit, unitFractionsType: FractionsType) {
        enemy.addComponent(Capture(unitFractionsType))
        getCapitalShipPosition(unitFractionsType)?.currentPosition?.let {
            enemy.addComponent(Teleport())
            enemy.addComponent(TargetPosition(it))
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(it))
        }
    }

}