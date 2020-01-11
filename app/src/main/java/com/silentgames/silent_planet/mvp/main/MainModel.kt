package com.silentgames.silent_planet.mvp.main

import android.content.Context
import com.silentgames.silent_planet.logic.CellRandomGenerator
import com.silentgames.silent_planet.logic.EntityRandomGenerator
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.system.RenderSystem
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.view.SurfaceGameView
import java.util.*

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel(val context: Context, private val surfaceView: SurfaceGameView) {

    fun generateNewBattleGround(firstTurnFraction: FractionsType): GameState = GameState(
            CellRandomGenerator(context).generateBattleGround(),
            EntityRandomGenerator(context).generateShips(),
            firstTurnFraction
    )

    fun getRenderSystem(onSceneUpdate: () -> kotlin.Unit) = RenderSystem(surfaceView, onSceneUpdate)

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
