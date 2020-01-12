package com.silentgames.silent_planet.mvp.main

import com.silentgames.core.logic.CellRandomGenerator
import com.silentgames.core.logic.EntityRandomGenerator
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.RenderSystem
import com.silentgames.silent_planet.view.SurfaceGameView

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel(private val surfaceView: SurfaceGameView) {

    fun generateNewBattleGround(firstTurnFraction: FractionsType): GameState = GameState(
            CellRandomGenerator().generateBattleGround(),
            EntityRandomGenerator().generateShips(),
            firstTurnFraction
    )

    fun getRenderSystem(onSceneUpdate: () -> Unit) = RenderSystem(surfaceView, onSceneUpdate)

}
