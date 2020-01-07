package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Hide
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.utils.notNull

class ExploreSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
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

    private fun makeCellExplored(hide: Hide, cell: Cell) {
        cell.addComponent(Texture(hide.bitmapToShow))
        cell.addComponent(hide.descriptionToShow)
        cell.removeComponent(hide)
    }

}