package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.silentgames.core.logic.CellRandomGenerator
import com.silentgames.core.logic.EntityRandomGenerator
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.RenderSystem

/**
 * Created by gidroshvandel on 22.06.17.
 */
class SilentPlanetModel(private val viewport: Viewport) {

    fun generateNewBattleGround(firstTurnFraction: FractionsType): GameState = GameState(
            CellRandomGenerator().generateBattleGround(),
            EntityRandomGenerator().generateUnits(),
            firstTurnFraction
    )

    fun getRenderSystem(
            onClick: (Axis) -> Unit
    ) = RenderSystem(
            viewport,
            SpriteBatch(),
            onClick
    )

}
