package com.silentgames.silent_planet.engine.base

import com.silentgames.silent_planet.engine.EngineAxis

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

    private var updated = false

    var mScaleFactor = 1f
    var scrollAxis = EngineAxis(0f, 0f)

    private var orient: Int = ORIENTATION_VERT

    fun setOrientation() {
        orient = if (this.width > this.height) {
            ORIENTATION_HOR
        } else {
            ORIENTATION_VERT
        }
    }

    fun setLayer(i: Int, layer: Layer) {
        updated = true
        if (i < layers.size) {
            layers[i] = layer
        }
    }

    fun getLayer(i: Int): Layer? {
        return if (i < layers.size) {
            layers[i]
        } else {
            null
        }
    }

    fun getLayers() = layers

    /**
     * Обновляет все соержимое сцены
     */
    fun update(onUpdated: ((Boolean) -> Unit)) {
        val updated = this.updated
        var updatedLayerCount = 0
        val dataSize = layers.size
        var somethingChange = false
        layers.forEach {
            it.update { changed ->
                updatedLayerCount++
                if (changed) {
                    somethingChange = true
                }
                if (dataSize == updatedLayerCount) {
                    this.updated = false
                    if (somethingChange || updated) {
                        onUpdated.invoke(somethingChange)
                    }
                }
            }
        }
    }
}
