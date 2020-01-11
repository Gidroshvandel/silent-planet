package com.silentgames.silent_planet.logic.ecs

data class Axis(val x: Int, val y: Int) {

    override fun toString(): String {
        return "Axis($x, $y)"
    }

}