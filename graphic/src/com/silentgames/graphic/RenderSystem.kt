package com.silentgames.graphic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.utils.viewport.Viewport
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
import com.silentgames.graphic.engine.*
import com.silentgames.graphic.engine.base.Layer
import com.silentgames.graphic.engine.base.Scene
import com.silentgames.graphic.engine.base.Sprite
import com.silentgames.graphic.mvp.InputMultiplexer


class RenderSystem(
        private val viewport: Viewport,
        private val batch: Batch,
        private val assets: Assets,
        private val onClick: (Axis) -> Unit
) : UnitSystem() {

    private val camera = (viewport.camera as OrthographicCamera)

    private var scene: Scene? = null

    private var engine: EngineEcs? = null

    private val width get() = viewport.worldHeight.toInt()
    private val height get() = viewport.worldHeight.toInt()

    private var stateTime = 0f

    override fun onEngineAttach(engine: EngineEcs) {
        scene = Scene(mutableListOf(Layer(), GridLayer(), Layer()), width, height)
        addClickListener()
        addZoomListener()
        this.engine = engine
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.removeComponent(Moving::class.java)
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)

        if (engine?.processing == false)
            render(gameState)

        engine?.processing = true

        viewport.camera.update()
        batch.projectionMatrix = viewport.camera.combined

        viewport.apply()

        batch.begin()

//        batch.draw(com.badlogic.gdx.graphics.Texture("space_texture.jpg"), 0f, 0f, camera.viewportWidth, camera.viewportHeight)
        stateTime += Gdx.graphics.deltaTime

        scene?.getLayers()?.forEach { layer ->
            for (tmp in layer.data) {
                tmp.draw(batch, width, height, stateTime)
            }
        }

        scene?.update {
            if (it)
                engine?.processing = false
        }

        batch.end()
    }

    private fun render(gameState: GameState) {
        val backgroundLayer = Layer()
        val entityLayer = Layer()
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells) {
            for (y in 0 until verticalCountOfCells) {
                gameState.getCell(Axis(x, y))?.toDrawBackground()?.let {
                    backgroundLayer.add(it)
                }

                val transport = gameState.getUnits(Axis(x, y)).firstOrNull { it.hasComponent<Transport>() }?.toDrawEntity()
                val entityToDraw = gameState.getUnitToDraw(Axis(x, y))?.toDrawEntity()

                if (transport != null) {
                    transport.first.addOn(entityLayer)
                    if (entityToDraw != null && entityToDraw.second) {
                        entityToDraw.first.addOn(entityLayer)
                    }
                } else {
                    entityToDraw?.first?.addOn(entityLayer)
                }

            }
        }
        scene?.setLayer(0, backgroundLayer)
        scene?.setLayer(2, entityLayer)
    }

    private fun GameState.getUnitToDraw(axis: Axis) =
            this.getUnits(axis).firstOrNull {
                val position = it.getComponent<Position>()
                position != null
                        && !position.moved
                        && position.currentPosition != position.oldPosition
                        && !it.hasComponent<Transport>()
            } ?: this.getUnits(axis).firstOrNull { !it.hasComponent<Transport>() }

    private fun Sprite.addOn(layer: Layer) {
        layer.add(this)
    }

    private fun CellEcs.toDrawBackground(): Background? {
        val arrow = getComponent<Arrow>()
        val textureId = getComponent<Texture>()?.bitmapName ?: return null
        val position = this.getCurrentPosition() ?: return null
        return if (arrow != null && isVisible()) {
            ArrowBackground(
                    position.toEngineAxis(),
                    textureId,
                    assets,
                    arrow.rotateAngle
            )
        } else {
            Background(
                    position.toEngineAxis(),
                    textureId,
                    assets
            )
        }
    }

    private fun UnitEcs.toDrawEntity(): Pair<Entity, Boolean>? {
        val textureId = this.getComponent<Texture>()?.bitmapName ?: return null
        val position = this.getComponent<Position>() ?: return null
        var isMoved = false
        val entity = Entity(
                this.id.toString(),
                position.currentPosition.toEngineAxis(),
                textureId,
                assets
        ).apply {
            if (!position.moved && position.oldPosition != position.currentPosition) {
                position.moved = true
                isMoved = true
                move(
                        position.oldPosition.toEngineAxis(),
                        position.currentPosition.toEngineAxis()
                )
            }
        }
        return Pair(entity, isMoved)
    }

    private fun Axis.toEngineAxis(): EngineAxis = EngineAxis(x.toFloat(), y.toFloat())

    private fun addClickListener() {
        InputMultiplexer.addProcessor(InputMouse(camera, onClick))
    }

    private fun addZoomListener() {
        camera.viewportHeight = viewport.worldHeight
        camera.viewportWidth = viewport.worldHeight
        camera.update()
        InputMultiplexer.addProcessor(GestureDetector(GameGestureAdapter(camera)))
    }

}