package com.silentgames.silent_planet.engine.base

import android.graphics.Bitmap
import android.graphics.Matrix
import com.silentgames.silent_planet.engine.EngineAxis


abstract class Sprite(axis: EngineAxis, var bmp: Bitmap) : Basic() {

    var axis: EngineAxis = axis
        set(value) {
            field = value
            refreshAll()
        }

    /**
     * Ширина (width) и высота (height) спрайта
     */
    var width = 0
    var height = 0
    /**
     * Матрица для отрисовки спрайта на канве
     */
    var matrix = Matrix()

    init {
        refreshAll()
    }

    /**
     * метод обновляет ширину, и высоту изображения вызывается после загрузки
     * спрайта и после изменения его размера. Также обновляет матрицу отрисовки
     */
    private fun refreshAll() {
        this.width = bmp.width
        this.height = bmp.height
        matrix.setTranslate(axis.x, axis.y)
    }

}