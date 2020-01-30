package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.g2d.Batch

abstract class Basic {

    open fun onResourceBufferAttached(resourceBuffer: ResourceBuffer) {
    }

    open fun update(onUpdated: ((Boolean) -> Unit)) {
        onUpdated.invoke(false)
    }

    internal abstract fun draw(batch: Batch, width: Int, height: Int, stateTime: Float)

}