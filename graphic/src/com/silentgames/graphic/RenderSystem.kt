package com.silentgames.graphic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
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

class RenderSystem(private val viewport: AppViewport, private val batch: Batch, private val onClick: (Axis) -> Unit) : UnitSystem() {

    private val mScaleFactor: Int = 1

    private var scene: Scene? = null

    private var engine: EngineEcs? = null

    private val width get() = viewport.worldWidth.toInt()
    private val height get() = viewport.worldHeight.toInt()

    override fun onEngineAttach(engine: EngineEcs) {
        scene = Scene(mutableListOf(Layer(), GridLayer(), Layer()), width, height)
        initClick()
        this.engine = engine
    }

    private fun initClick() {
        Gdx.input.inputProcessor = InputMouse { axis ->
            scene?.let {
                val eventX = (axis.x + (it.scrollAxis.x)) / mScaleFactor
                val eventY = (axis.y + (it.scrollAxis.y)) / mScaleFactor
                val x = ((Constants.horizontalCountOfCells) * eventX / viewport.screenWidth).toInt()
                val y = ((Constants.verticalCountOfCells) * eventY / viewport.screenHeight).toInt()
                onClick(Axis(x, y))
            }
        }
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)

        if (engine?.processing == false)
            render(gameState)

        engine?.processing = true

        viewport.camera.update()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()

//        batch.draw(com.badlogic.gdx.graphics.Texture("space_texture.jpg"), 0f, 0f, camera.viewportWidth, camera.viewportHeight)

        scene?.getLayers()?.forEach { layer ->
            for (tmp in layer.data) {
                tmp.draw(batch, width, height)
            }
        }

        scene?.update {
            if (it)
                engine?.processing = false
        }

        batch.end()


    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.removeComponent(Moving::class.java)
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
        scene?.setLayer(0, backgroundLayer)
        scene?.setLayer(2, entityLayer)
    }

    private fun CellEcs.toDrawBackground(): Background? {
        val arrow = getComponent<Arrow>()
        val textureId = getComponent<Texture>()?.bitmapName ?: return null
        val position = this.getCurrentPosition() ?: return null
        return if (arrow != null && isVisible()) {
            ArrowBackground(
                    position.toEngineAxis(),
                    textureId,
                    arrow.rotateAngle
            )
        } else {
            Background(
                    position.toEngineAxis(),
                    textureId
            )
        }
    }

    private fun UnitEcs.toDrawEntity(): Entity? {
        val textureId = this.getComponent<Texture>()?.bitmapName ?: return null
        val position = this.getComponent<Position>() ?: return null
        return Entity(
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