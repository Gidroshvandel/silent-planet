package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Texture
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class ExploreSystem : System {

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
    }

}