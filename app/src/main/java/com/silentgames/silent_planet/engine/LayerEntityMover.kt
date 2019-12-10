package com.silentgames.silent_planet.engine

import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.engine.base.Scene

class LayerEntityMover(private val position: Int, private val newLayer: Layer) {

    fun attach(scene: Scene) {
        val oldLayer = scene.getLayer(position)
        if (oldLayer != null) {
            if (newLayer.data.firstOrNull { it is Entity } != null) {
                scene.setLayer(position, moveChangedEntities(oldLayer, newLayer))
            } else {
                scene.setLayer(position, newLayer)
            }
        } else {
            scene.setLayer(position, newLayer)
        }
    }

    private fun moveChangedEntities(oldLayer: Layer, newLayer: Layer): Layer {
        val oldLayerData = oldLayer.data.filterIsInstance<Entity>()
        val newLayerData = newLayer.data.filterIsInstance<Entity>()

        val changed = mutableListOf<Entity>()

        oldLayerData.forEach { oldEntity ->
            newLayerData.forEach { newEntity ->
                if (oldEntity == newEntity && oldEntity.axis != newEntity.axis) {
                    newEntity.move(oldEntity.axis, newEntity.axis)
                    changed.add(newEntity)
                }
            }
        }

        newLayerData.subtract(changed).forEach {
            it.move(it.axis)
        }

        return newLayer
    }

}