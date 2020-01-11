package com.silentgames.silent_planet.logic.ecs.system

import android.graphics.Bitmap
import android.util.SparseArray
import com.silentgames.silent_planet.engine.Background
import com.silentgames.silent_planet.engine.EngineAxis
import com.silentgames.silent_planet.engine.Entity
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.EngineEcs
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Arrow
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.cell.CellEcs
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs
import com.silentgames.silent_planet.logic.ecs.extractTransports
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.view.SurfaceGameView

class RenderSystem(private val surfaceView: SurfaceGameView, private val onSceneUpdate: () -> Unit) : System {

    private var engine: EngineEcs? = null

    private var bitmapCache: SparseArray<Bitmap> = SparseArray()

    private fun getById(id: Int, initBitmap: (id: Int) -> Bitmap): Bitmap {
        val bitmapCache = bitmapCache.get(id)
        return if (bitmapCache != null) {
            bitmapCache
        } else {
            val bitmap = initBitmap(id)
            this.bitmapCache.put(id, bitmap)
            bitmap
        }
    }

    private fun CellEcs.getBitmap(): Bitmap? {
        val texture = this.getComponent<Texture>()
        if (texture != null) {
            val bitmap = getById(texture.bitmapId) {
                BitmapEditor.getCellBitmap(surfaceView.context, it)
            }
            val arrow = this.getComponent<Arrow>()
            return if (arrow != null) {
                BitmapEditor.rotateBitmap(arrow.rotateAngle, bitmap)
            } else {
                bitmap
            }
        }
        return null
    }

    private fun UnitEcs.getBitmap(): Bitmap? {
        val texture = this.getComponent<Texture>()
        if (texture != null) {
            return getById(texture.bitmapId) {
                BitmapEditor.getEntityBitmap(surfaceView.context, it)
            }
        }
        return null
    }

    override fun onEngineAttach(engine: EngineEcs) {
        this.engine = engine
    }

    override fun execute(gameState: GameState) {
        engine?.processing = true
        render(gameState) {
            engine?.processing = false
            onSceneUpdate.invoke()
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
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

    private fun render(gameState: GameState, onUpdateComplete: (Boolean) -> Unit) {
        val backgroundLayer = Layer()
        val entityLayer = Layer()
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells) {
            for (y in 0 until verticalCountOfCells) {
                backgroundLayer.add(Background(
                        EngineAxis(x.toFloat(), y.toFloat()),
                        gameState.getCell(Axis(x, y))?.getBitmap()!!
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

    private fun UnitEcs.toDrawEntity(): Entity? {
        val texture = this.getBitmap() ?: return null
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