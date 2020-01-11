package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs
import com.silentgames.silent_planet.utils.notNull

class CaptureSystem : System {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        notNull(
                unit.getComponent(),
                unit.getComponent(),
                unit,
                gameState,
                ::capture
        )
    }

    private fun capture(position: Position, unitFractionsType: FractionsType, unit: UnitEcs, gameState: GameState) {
        val enemyUnits = gameState.getUnits(position.currentPosition).filterNot {
            val fractionsType = it.getComponent<FractionsType>()
            fractionsType == null
                    || fractionsType == unitFractionsType
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

    private fun GameState.captureUnitByEnemy(unit: UnitEcs, enemyUnits: List<UnitEcs>) {
        enemyUnits.first().getComponent<FractionsType>()?.let { enemyFractionsType ->
            unit.addComponent(Capture(enemyFractionsType))
            getCapitalShipPosition(enemyFractionsType)?.currentPosition?.let { shipPosition ->
                unit.addComponent(Teleport())
                unit.addComponent(TargetPosition(shipPosition))
                unit.removeComponent(Active::class.java)
                enemyUnits.forEach {
                    it.addComponent(Teleport())
                    it.addComponent(TargetPosition(shipPosition))
                }
            }
        }
    }

    private fun GameState.captureEnemyByUnit(enemy: UnitEcs, unit: UnitEcs, unitFractionsType: FractionsType) {
        enemy.addComponent(Capture(unitFractionsType))
        getCapitalShipPosition(unitFractionsType)?.currentPosition?.let {
            enemy.addComponent(Teleport())
            enemy.addComponent(TargetPosition(it))
            enemy.removeComponent(Active::class.java)
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(it))
        }
    }

}