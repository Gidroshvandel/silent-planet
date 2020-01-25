package com.silentgames.silent_planet.logic

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.UnitSystem
import com.silentgames.core.logic.ecs.system.getCurrentPosition
import com.silentgames.core.logic.ecs.system.isVisible
import com.silentgames.silent_planet.engine.ArrowBackground
import com.silentgames.silent_planet.engine.Background
import com.silentgames.silent_planet.engine.EngineAxis
import com.silentgames.silent_planet.engine.Entity
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.view.SurfaceGameView

class RenderSystem(private val surfaceView: SurfaceGameView, private val onSceneUpdate: () -> Unit) : UnitSystem() {

    private var engine: EngineEcs? = null

    override fun onEngineAttach(engine: EngineEcs) {
        this.engine = engine
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)
        engine?.processing = true
        render(gameState) {
            if (it)
                engine?.processing = false
            else {
                onSceneUpdate.invoke()
                engine?.processSystems()
            }
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.removeComponent<Moving>()
    }

    private fun render(gameState: GameState, onUpdateComplete: (Boolean) -> Unit) {
        val backgroundLayer = Layer()
        val entityLayer = Layer()
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells) {
            for (y in 0 until verticalCountOfCells) {
                gameState.getCell(Axis(x, y))?.toDrawBackground()?.let {
                    backgroundLayer.add(it)
                }

                val transport = gameState.getUnits(Axis(x, y)).firstOrNull { it.hasComponent<Transport>() }

                val entityToDraw = gameState.getUnits(Axis(x, y)).firstOrNull {
                    val position = it.getComponent<Position>()
                    position != null
                            && !position.moved
                            && position.currentPosition != position.oldPosition
                            && !it.hasComponent<Transport>()
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

    private fun CellEcs.toDrawBackground(): Background? {
        val arrow = getComponent<Arrow>()
        val textureId = getComponent<Texture>()?.bitmapName ?: return null
        val position = this.getCurrentPosition() ?: return null
        return if (arrow != null && isVisible()) {
            ArrowBackground(
                    surfaceView.context,
                    position.toEngineAxis(),
                    textureId,
                    arrow.rotateAngle
            )
        } else {
            Background(
                    surfaceView.context,
                    position.toEngineAxis(),
                    textureId
            )
        }
    }

    private fun UnitEcs.toDrawEntity(): Entity? {
        val textureId = this.getComponent<Texture>()?.bitmapName ?: return null
        val position = this.getComponent<Position>() ?: return null
        return Entity(
                surfaceView.context,
                this.id.toString(),
                position.currentPosition.toEngineAxis(),
                textureId
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