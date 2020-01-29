package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

class ResourceBuffer {

    private var bitmapBuffer: HashMap<Int, Array<TextureRegion>> = HashMap()

    fun put(id: Int, bitmap: Array<TextureRegion>) {
        bitmapBuffer[id] = bitmap
    }

    fun getList(id: Int): Array<TextureRegion>? =
            bitmapBuffer.get(id)

    fun clear() {
        bitmapBuffer.clear()
    }

}