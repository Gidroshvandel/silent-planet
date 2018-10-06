package com.silentgames.silent_planet.engine

import com.silentgames.silent_planet.engine.base.Layer

class GridLayer(canvasSize: Float) : Layer() {

    init {
        add(Grid(canvasSize))
    }

}