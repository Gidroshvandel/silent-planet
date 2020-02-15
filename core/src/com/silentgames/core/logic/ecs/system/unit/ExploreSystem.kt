package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Texture
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentPosition
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.utils.notNull

class ExploreSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "ExploreSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        val position = unit.getComponent<Position>()?.currentPosition
        if (position != null) {
            val cell = gameState.getCell(position)
            notNull(
                    cell?.getComponent(),
                    cell,
                    ::makeCellExplored
            )
        }
    }

    private fun makeCellExplored(hide: Hide, cell: CellEcs) {
        cell.addComponent(Texture(hide.imageToShow))
        cell.addComponent(hide.descriptionToShow)
        cell.removeComponent(hide)
        CoreLogger.logDebug(
                SYSTEM_TAG,
                "${cell.getName()} ${cell.getCurrentPosition().toString()}"
        )
    }

}