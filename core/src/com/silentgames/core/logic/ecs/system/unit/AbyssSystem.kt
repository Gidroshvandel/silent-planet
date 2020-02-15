package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.MotionType
import com.silentgames.core.logic.ecs.component.Abyss
import com.silentgames.core.logic.ecs.component.Route
import com.silentgames.core.logic.ecs.entity.event.TeleportEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentUnitCell
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.utils.notNull

class AbyssSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "AbyssSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            cell.getComponent<Abyss>()?.let {
                notNull(
                        gameState,
                        unit.getComponent<Route>()?.paths,
                        unit,
                        ::abyss
                )
            }
        }
    }

    private fun abyss(gameState: GameState, paths: MutableList<Motion>, unit: UnitEcs) {
        this.getTarget(paths)?.let {
            gameState.addEvent(TeleportEvent(it, unit))
            CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} teleport to $it")
        }
    }

    private fun getTarget(paths: MutableList<Motion>): Axis? {
        paths.reversed().forEach {
            if (it.motionType == MotionType.MOVEMENT) {
                return it.axis
            }
        }
        return null
    }
}