package com.silentgames.silent_planet.model.fractions

/**
 * Created by gidroshvandel on 26.09.16.
 */
enum class FractionsEnum {
    Aliens, Humans, Pirates, Robots;

    operator fun next(): FractionsEnum {
        return vals[(this.ordinal + 1) % vals.size]
    }

    companion object {

        private val vals = values()
    }
}

