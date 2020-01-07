package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.engine.Background
import com.silentgames.silent_planet.engine.EngineAxis
import com.silentgames.silent_planet.engine.Entity
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.ecs.Engine
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.view.SurfaceGameView

class RenderSystem(private val surfaceView: SurfaceGameView) : System {

    private var engine: Engine? = null

    override fun onEngineAttach(engine: Engine) {
        this.engine = engine
    }

    override fun execute(gameState: GameState) {
        render(gameState) {}
    }

    override fun execute(gameState: GameState, unit: Unit) {
        render(gameState) {
            engine?.processSystems(unit)
        }
    }

    private fun render(gameState: GameState, onUpdateComplete: () -> kotlin.Unit) {
        val backgroundLayer = Layer()
        val entityLayer = Layer()
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells) {
            for (y in 0 until verticalCountOfCells) {
                backgroundLayer.add(Background(
                        EngineAxis(x.toFloat(), y.toFloat()),
                        gameState.getCell(Axis(x, y))?.getComponent<Texture>()?.bitmap!!
                ))
                val entity = gameState.getUnit(Axis(x, y))
                val texture = entity?.getComponent<Texture>()?.bitmap
                if (entity != null && texture != null) {
                    entityLayer.add(
                            Entity(
                                    entity.id.toString(),
                                    EngineAxis(x.toFloat(), y.toFloat()),
                                    texture
                            )
                    )
                }
            }
        }
        surfaceView.updateLayer(SurfaceGameView.LayerType.BACKGROUND, backgroundLayer)
        surfaceView.updateLayer(SurfaceGameView.LayerType.ENTITY, entityLayer, onUpdateComplete)
    }

}