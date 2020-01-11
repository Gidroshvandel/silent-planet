package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.engine.Background
import com.silentgames.silent_planet.engine.EngineAxis
import com.silentgames.silent_planet.engine.Entity
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.ecs.Engine
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.ecs.extractTransports
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.view.SurfaceGameView

class RenderSystem(private val surfaceView: SurfaceGameView, private val onSceneUpdate: () -> kotlin.Unit) : System {

    private var engine: Engine? = null

    override fun onEngineAttach(engine: Engine) {
        this.engine = engine
    }

    override fun execute(gameState: GameState) {
        engine?.processing = true
        render(gameState) {
            engine?.processing = false
            onSceneUpdate.invoke()
        }
    }

    override fun execute(gameState: GameState, unit: Unit) {
        engine?.processing = true
        render(gameState) {
            if (it) {
                engine?.forceProcessSystem(unit)
            } else {
                engine?.processing = false
            }
            onSceneUpdate.invoke()
        }
    }

    private fun render(gameState: GameState, onUpdateComplete: (Boolean) -> kotlin.Unit) {
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

                val transport = gameState.getUnits(Axis(x, y)).firstOrNull { it.hasComponent<Transport>() }

                val entityToDraw = gameState.getUnits(Axis(x, y)).extractTransports().firstOrNull {
                    val position = it.getComponent<Position>()
                    position != null && !position.moved && position.currentPosition != position.oldPosition
                } ?: gameState.getUnits(Axis(x, y)).firstOrNull { !it.hasComponent<Transport>() }

                entityToDraw?.toDrawEntity()?.let {
                    entityLayer.add(it)
                }
                transport?.toDrawEntity()?.let {
                    entityLayer.add(it)
                }


            }
        }
        surfaceView.updateLayer(SurfaceGameView.LayerType.BACKGROUND, backgroundLayer)
        surfaceView.updateLayer(SurfaceGameView.LayerType.ENTITY, entityLayer, onUpdateComplete)
    }

    private fun Unit.toDrawEntity(): Entity? {
        val texture = this.getComponent<Texture>()?.bitmap ?: return null
        val position = this.getComponent<Position>() ?: return null
        return Entity(
                this.id.toString(),
                position.currentPosition.toEngineAxis(),
                texture
        ).apply {
            if (!position.moved && position.oldPosition != position.currentPosition) {
                position.moved = true
                move(
                        position.oldPosition.toEngineAxis(),
                        position.currentPosition.toEngineAxis()
                )
            }
        }
    }

    private fun Axis.toEngineAxis(): EngineAxis = EngineAxis(x.toFloat(), y.toFloat())

}