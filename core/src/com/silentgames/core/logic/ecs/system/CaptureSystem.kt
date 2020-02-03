package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class CaptureSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<Active>()?.let {
            notNull(
                    unit.getComponent<Position>()?.currentPosition,
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::capture
            )
        }
    }

    private fun capture(position: Axis, unitFractionsType: FractionsType, unit: UnitEcs, gameState: GameState) {
        if (gameState.getCapitalShipPosition(unitFractionsType)?.currentPosition != position) {
            val enemies = getEnemies(gameState, position, unitFractionsType)
            if (enemies.isNotEmpty()) {
                if (enemies.size > 1) {
                    enemies.first().getComponent<FractionsType>()?.let {
                        gameState.captureUnit(unit, enemies, it)
                    }
                } else {
                    gameState.captureUnit(enemies.first(), listOf(unit), unitFractionsType)
                }
            }
        }
    }

    private fun getEnemies(gameState: GameState, position: Axis, unitFractionsType: FractionsType): List<UnitEcs> {
        return gameState.getUnits(position).filterNot {
            val fractionsType = it.getComponent<FractionsType>()
            fractionsType == null
                    || fractionsType == unitFractionsType
                    || it.hasComponent<CapitalShip>()
                    || !it.hasComponent<Active>()
        }
    }

    private fun GameState.captureUnit(captured: UnitEcs, enemies: List<UnitEcs>, fractionsType: FractionsType) {
        CoreLogger.logDebug(
                "CaptureSystem",
                "captured ${captured.getComponent<Description>()?.name} ${captured.getComponent<Position>()?.currentPosition.toString()}"
        )
        captured.addComponent(Capture(fractionsType))
        getCapitalShipPosition(fractionsType)?.currentPosition?.let {
            captured.removeComponent(Active::class.java)
            val enemiesWithCaptured = enemies.toMutableList().apply { add(captured) }
            moveUnits(enemiesWithCaptured, it)
        }
    }

    private fun moveUnits(units: List<UnitEcs>, position: Axis) {
        units.forEach {
            it.addComponent(Teleport())
            it.addComponent(TargetPosition(position))
        }
    }
}