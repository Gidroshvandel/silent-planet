package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.silentgames.graphic.engine.BitmapBuffer
import com.silentgames.graphic.engine.EngineAxis

abstract class Sprite(axis: EngineAxis,
                      private val bmpResourceId: String) : Basic() {

    var axis: EngineAxis = axis
        set(value) {
            field = value
        }

    private var resized = false

    protected open fun initBitmap(bmpResourceId: String): Sprite {
        return Sprite(Texture(bmpResourceId)).apply {
            flip(false, true)
        }
    }

    protected open fun getBitmap(): Sprite {
        val bitmapCache = BitmapBuffer.get(getBitmapId())
        return if (bitmapCache != null) {
            bitmapCache
        } else {
            val bitmap = initBitmap(bmpResourceId)
            BitmapBuffer.put(getBitmapId(), bitmap)
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
            BitmapBuffer.put(getBitmapId(), bitmap)
            bitmap
        }
    }

    protected open fun getBitmapId(): Int = bmpResourceId.hashCode()

}