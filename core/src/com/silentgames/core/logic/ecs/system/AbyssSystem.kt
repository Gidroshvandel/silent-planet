package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class AbyssSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            cell.getComponent<Abyss>()?.let {
                notNull(unit.getComponent<Route>()?.paths,
                        unit,
                        gameState,
                        ::abyss)
            }
        }
    }

    private fun abyss(paths: MutableList<Motion>, unit: UnitEcs, gameState: GameState) {
        this.getTarget(paths, gameState)?.let {
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(it))
        }
    }

    fun getTarget(paths: MutableList<Motion>, gameState: GameState): Axis? {
        paths.reversed().forEach {
            if (!this.isAbyss(it.axis, gameState) || !this.isArrow(it.axis, gameState)) {
                return it.axis
            }
        }
        return null
    }

    private fun isArrow(axis: Axis, gameState: GameState): Boolean {
        return gameState.getCell(axis)?.getComponent<Arrow>() != null
    }

    private fun isAbyss(axis: Axis, gameState: GameState): Boolean {
        return gameState.getCell(axis)?.getComponent<Abyss>() != null
    }
}