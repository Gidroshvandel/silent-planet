package com.silentgames.silent_planet.engine.base

import android.graphics.Canvas
import android.graphics.Paint

abstract class Basic {

    open fun update(onUpdated: ((Boolean) -> Unit)) {
        onUpdated.invoke(false)
    }

    /**
     * Метод отрисовки
     * @param canvas - канва для отрисовки
     * @param paint - кисть для отрисовки
     */
    internal abstract fun draw(canvas: Canvas, paint: Paint)
}
