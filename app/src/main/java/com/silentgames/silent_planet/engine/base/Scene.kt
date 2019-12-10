package com.silentgames.silent_planet.engine.base

import com.silentgames.silent_planet.model.Axis


class Scene(
        /**
         * Массив слоев на сцене
         */
        private val layers: MutableList<Layer>,
        /**
         * высота и ширина сцены
         */
        val width: Int,
        val height: Int
) {

    companion object {
        const val ORIENTATION_VERT = 0
        const val ORIENTATION_HOR = 1
    }

    var mScaleFactor = 1f
    var scrollAxis = Axis(0, 0)

    private var orient: Int = ORIENTATION_VERT

    fun setOrientation() {
        orient = if (this.width > this.height) {
            ORIENTATION_HOR
        } else {
            ORIENTATION_VERT
        }
    }

    fun setLayer(i: Int, layer: Layer) {
        if (i < layers.size) {
            layers[i] = layer
        }
    }

    fun getLayers() = layers

    /**
     * Обновляет все соержимое сцены
     */
    fun update() {
        for (l in layers) {
            l.update()
        }
    }

}