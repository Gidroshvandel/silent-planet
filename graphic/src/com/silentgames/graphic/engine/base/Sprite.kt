package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.silentgames.core.logic.Constants
import com.silentgames.graphic.Assets
import com.silentgames.graphic.engine.EngineAxis
import com.silentgames.graphic.scaleImageForBoard

abstract class Sprite(
    axis: EngineAxis,
    protected val bmpResourceId: String,
    private val assets: Assets
) : Basic() {

    protected var runningAnimation: Animation<TextureRegion>? = null
        private set

    private lateinit var resourceBuffer: ResourceBuffer

    var axis: EngineAxis = axis
        set(value) {
            field = value
        }

    private var resized = false

    override fun onResourceBufferAttached(resourceBuffer: ResourceBuffer) {
        this.resourceBuffer = resourceBuffer
        runningAnimation = initAnimation(getBitmap())
    }

    abstract fun initAnimation(textures: Array<TextureRegion>): Animation<TextureRegion>

    protected open fun initBitmap(bmpResourceId: String): Array<TextureRegion> {
        return Array<TextureRegion>().apply {
            assets.getTextureRegions(bmpResourceId).forEach {
                val texture = TextureRegion(it)
                texture.flip(false, true)
                add(texture)
            }
        }
    }

    protected open fun getBitmap(): Array<TextureRegion> {
        val bitmapCache = resourceBuffer.getList(getBitmapId())
        return if (bitmapCache != null) {
            bitmapCache
        } else {
            val bitmap = initBitmap(bmpResourceId)
            resourceBuffer.put(getBitmapId(), bitmap)
            bitmap
        }
    }

    protected open fun getBitmapId(): Int = bmpResourceId.hashCode()

    protected open fun getSize(textureRegion: TextureRegion, batchSize: Int) =
        scaleImageForBoard(
            textureRegion.regionWidth.toFloat(),
            textureRegion.regionHeight.toFloat(),
            batchSize.toFloat()
        )

    protected open fun getCoordinates(
        axis: EngineAxis,
        width: Int,
        height: Int,
        textureRegion: TextureRegion
    ): EngineAxis {
        val x = cellCenterNumeratorSquare(
            axis.x,
            width,
            getSize(textureRegion, width).x,
            Constants.verticalCountOfCells
        )
        val y = cellCenterNumeratorSquare(
            axis.y,
            height,
            getSize(textureRegion, height).y,
            Constants.horizontalCountOfCells
        )
        return EngineAxis(x, y)
    }

    private fun cellCenterNumeratorSquare(
        cell: Float,
        batchSize: Int,
        viewSize: Float,
        lineCountOfCells: Int
    ): Float {
        return cellCenterNumeratorPoint(cell, batchSize, lineCountOfCells) - viewSize / 2
    }

    private fun cellCenterNumeratorPoint(
        cell: Float,
        batchSize: Int,
        lineCountOfCells: Int
    ): Float {
        return 1f / (2 * lineCountOfCells) * batchSize + 1f / lineCountOfCells * cell * batchSize
    }
}
