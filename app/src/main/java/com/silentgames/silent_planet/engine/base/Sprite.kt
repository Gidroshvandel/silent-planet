package com.silentgames.silent_planet.engine.base

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.silentgames.silent_planet.engine.BitmapBuffer
import com.silentgames.silent_planet.engine.EngineAxis

abstract class Sprite(val context: Context,
                      axis: EngineAxis,
                      private val bmpResourceId: Int) : Basic() {

    var axis: EngineAxis = axis
        set(value) {
            field = value
        }

    private var resized = false

    protected open fun initBitmap(bmpResourceId: Int): Bitmap =
            BitmapFactory.decodeResource(context.resources, bmpResourceId)

    protected open fun getBitmap(): Bitmap {
        val bitmapCache = BitmapBuffer.get(getBitmapId())
        return if (bitmapCache != null) {
            bitmapCache
        } else {
            val bitmap = initBitmap(bmpResourceId)
            BitmapBuffer.put(getBitmapId(), bitmap)
            bitmap
        }
    }

    protected fun getResizedBitmap(width: Float, height: Float): Bitmap {
        val bitmap = getBitmap()
        return if (resized) {
            bitmap
        } else {
            resized = true
            val resizedBitmap = bitmap.resize(width, height)
            BitmapBuffer.put(getBitmapId(), resizedBitmap)
            resizedBitmap
        }
    }

    private fun Bitmap.resize(newWidth: Float, newHeight: Float): Bitmap {
        val width = this.width
        val height = this.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    protected open fun getBitmapId(): Int = bmpResourceId

}