package com.silentgames.graphic.engine.base

import com.badlogic.gdx.graphics.g2d.Batch

abstract class Basic {

    open fun update(onUpdated: ((Boolean) -> Unit)) {
        onUpdated.invoke(false)
    }

    /**
     * Метод отрисовки
     * @param canvas - канва для отрисовки
     * @param paint - кисть для отрисовки
     */
    internal abstract fun draw(batch: Batch, width: Int, height: Int)

}