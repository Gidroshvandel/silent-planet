package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.MotionType
import com.silentgames.core.logic.ecs.component.Abyss
import com.silentgames.core.logic.ecs.component.Route
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class AbyssSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            cell.getComponent<Abyss>()?.let {
                notNull(unit.getComponent<Route>()?.paths,
                        unit,
                        ::abyss)
            }
        }
    }

    private fun abyss(paths: MutableList<Motion>, unit: UnitEcs) {
        this.getTarget(paths)?.let {
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(it))
        }
    }

    fun getTarget(paths: MutableList<Motion>): Axis? {
        paths.reversed().forEach {
            if (it.motionType == MotionType.MOVEMENT) {
                return it.axis
            }
        }
        return null
    }
}