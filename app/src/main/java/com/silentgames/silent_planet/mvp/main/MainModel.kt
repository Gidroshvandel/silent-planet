package com.silentgames.silent_planet.mvp.main

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.CellRandomGenerator
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.ecs.system.RenderSystem
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.view.SurfaceGameView
import java.util.*

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel(val context: Context, private val surfaceView: SurfaceGameView) {

    suspend fun generateNewBattleGround(): GameState {
        val cells = CellRandomGenerator(context).generateBattleGround()
//        EntityRandomGenerator(context).spawnShips(gameMatrix)
        return GameState(
                cells.toMutableList(),
                mutableListOf(Unit(context, Position(Axis(0, 0)), Texture(BitmapEditor.getEntityBitmap(context, R.drawable.aliens_space_ship))))
        )
    }

    fun getRenderSystem() = RenderSystem(surfaceView)

    fun getPlayersNameOnCell(gameMatrixCell: Cell): List<String> {
        val data = ArrayList<String>()

        if (gameMatrixCell.entityType.isNotEmpty()) {
            for (entityType in gameMatrixCell.entityType) {
                if (entityType is SpaceShip) {
                    entityType.playersOnBord.forEach { data.add(it.name) }
                } else if (entityType is Player) {
                    data.add(entityType.name)
                }
            }
        }
        return data
    }

}
