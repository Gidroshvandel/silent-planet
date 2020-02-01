package com.silentgames.graphic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
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

    val camera = (viewport.camera as OrthographicCamera)

    private var scaleFactor: Float = 1f

    private var scene: Scene? = null

    private var engine: EngineEcs? = null

    private val width get() = viewport.worldHeight.toInt()
    private val height get() = viewport.worldHeight.toInt()

    override fun onEngineAttach(engine: EngineEcs) {
        scene = Scene(mutableListOf(Layer(), GridLayer(), Layer()), width, height)
        initClick()
        addZoomListener()
        this.engine = engine
    }

    private fun initClick() {
        InputMultiplexer.addProcessor(InputMouse { axis ->
            scene?.let {
                val unProjectAxis = camera.unProject(Vector2(axis.x.toFloat(), axis.y.toFloat()))
                val x = ((Constants.horizontalCountOfCells) * unProjectAxis.x / camera.viewportWidth).toInt()
                val y = ((Constants.verticalCountOfCells) * unProjectAxis.y / camera.viewportHeight).toInt()
                println("x = " + x + "y = " + y)

                if (x < Constants.horizontalCountOfCells && y < Constants.verticalCountOfCells) {
                    onClick(Axis(x, y))
                }
            }
        })
    }


    private var stateTime = 0f

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

    private fun addZoomListener() {
        camera.viewportHeight = viewport.worldHeight
        camera.viewportWidth = viewport.worldHeight
        camera.update()
        InputMultiplexer.addProcessor(GestureDetector(object : GestureDetector.GestureAdapter() {

            private var isNowPinch = false
            private var zoomPoint: Vector2? = null

            override fun zoom(initialDistance: Float, distance: Float): Boolean {
                val ratio: Float = initialDistance / distance

                var zoom = scaleFactor * ratio

                if (zoom > 1) {
                    zoom = 1f
                } else if (zoom < 0.5) {
                    zoom = 0.5f
                }
                camera.zoom = zoom
                checkCameraBorders()
                return false
            }

            override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
                if (count == 2) {
                    if (scaleFactor < 1f) {
                        camera.zoom = 1f
                        scaleFactor = camera.zoom
                    } else {
                        camera.zoom = 0.5f
                        scaleFactor = camera.zoom
                        val vector = camera.unProject(Vector2(x, y))
                        camera.position.x = vector.x
                        camera.position.y = vector.y
                    }
                    checkCameraBorders()
                    return true
                }
                return false
            }


            override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
                if (!isNowPinch) {
                    camera.translate(-deltaX * 0.5f, -deltaY * 0.5f)
                    checkCameraBorders()
                }
                return false
            }


            override fun pinch(initialPointer1: Vector2, initialPointer2: Vector2, pointer1: Vector2, pointer2: Vector2): Boolean {
                isNowPinch = true
                val initialPointerFirst = camera.unProject(initialPointer1)
                val initialPointerSecond = camera.unProject(initialPointer2)
                if (zoomPoint == null) {
                    zoomPoint = Vector2((initialPointerFirst.x + initialPointerSecond.x) / 2, (initialPointerFirst.y + initialPointerSecond.y) / 2)
                }
                zoomPoint?.let {
                    camera.position.x = it.x
                    camera.position.y = it.y
                }
                return false
            }

            override fun pinchStop() {
                isNowPinch = false
                zoomPoint = null
                scaleFactor = camera.zoom
                super.pinchStop()
            }

            private fun checkCameraBorders() {
                val effectiveViewportWidth: Float = camera.viewportWidth * camera.zoom
                val effectiveViewportHeight: Float = camera.viewportHeight * camera.zoom
                camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, width - effectiveViewportWidth / 2f)
                camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, height - effectiveViewportHeight / 2f)
            }


        }))
    }

    private fun Camera.unProject(vector: Vector2) =
            unproject(vector.toVector3(), 0f, 0f, Gdx.graphics.height.toFloat(), Gdx.graphics.height.toFloat())

    private fun Vector2.toVector3() = Vector3(x, y, 0f)


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

}