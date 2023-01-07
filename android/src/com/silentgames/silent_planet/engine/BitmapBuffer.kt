package com.silentgames.silent_planet.engine

import android.graphics.Bitmap
import android.util.SparseArray

object BitmapBuffer {

    private var bitmapBuffer: SparseArray<Bitmap> = SparseArray()

    fun put(id: Int, bitmap: Bitmap) {
        bitmapBuffer.put(id, bitmap)
    }

    fun get(id: Int): Bitmap? =
        bitmapBuffer.get(id)

    fun clear() {
        bitmapBuffer.clear()
    }
}
