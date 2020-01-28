package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.g2d.Sprite
import com.silentgames.graphic.Assets
import com.silentgames.graphic.engine.EngineAxis

abstract class Sprite(axis: EngineAxis,
                      protected val bmpResourceId: String,
                      private val assets: Assets) : Basic() {

    private lateinit var resourceBuffer: ResourceBuffer

    var axis: EngineAxis = axis
        set(value) {
            field = value
        }

    private var resized = false

    override fun onResourceBufferAttached(resourceBuffer: ResourceBuffer) {
        this.resourceBuffer = resourceBuffer
    }

    protected open fun initBitmap(bmpResourceId: String): Sprite {
        return assets.getSprite(bmpResourceId).apply {
            flip(false, true)
        }
    }

    protected open fun getBitmap(): Sprite {
        val bitmapCache = resourceBuffer.get(getBitmapId())
        return if (bitmapCache != null) {
            bitmapCache
        } else {
            val bitmap = initBitmap(bmpResourceId)
            resourceBuffer.put(getBitmapId(), bitmap)
            bitmap
        }
    }

    protected open fun getResizedBitmap(width: Float, height: Float): Sprite {
        val bitmap = getBitmap()
        return if (resized) {
            bitmap
        } else {
            resized = true
            bitmap.setSize(width, height)
            resourceBuffer.put(getBitmapId(), bitmap)
            bitmap
        }
    }

    protected open fun getBitmapId(): Int = bmpResourceId.hashCode()

}