package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.g2d.Sprite

class ResourceBuffer {

    private var bitmapBuffer: HashMap<Int, Sprite> = HashMap()

    fun put(id: Int, bitmap: Sprite) {
        bitmapBuffer.put(id, bitmap)
    }

    fun get(id: Int): Sprite? =
            bitmapBuffer.get(id)

    fun clear() {
        bitmapBuffer.clear()
    }

}