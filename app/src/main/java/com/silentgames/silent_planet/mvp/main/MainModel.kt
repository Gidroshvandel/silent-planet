package com.silentgames.silent_planet.mvp.main

import android.content.Context
import com.silentgames.silent_planet.logic.CellRandomGenerator
import com.silentgames.silent_planet.logic.EntityRandomGenerator
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.system.RenderSystem
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.view.SurfaceGameView

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel(val context: Context, private val surfaceView: SurfaceGameView) {

    fun generateNewBattleGround(firstTurnFraction: FractionsType): GameState = GameState(
            CellRandomGenerator(context).generateBattleGround(),
            EntityRandomGenerator(context).generateShips(),
            firstTurnFraction
    )

    fun getRenderSystem(onSceneUpdate: () -> Unit) = RenderSystem(surfaceView, onSceneUpdate)

}
