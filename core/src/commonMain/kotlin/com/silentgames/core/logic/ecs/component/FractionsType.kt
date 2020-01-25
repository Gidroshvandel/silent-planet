package com.silentgames.core.logic.ecs.component

/**
 * Created by gidroshvandel on 26.09.16.
 */
enum class FractionsType : Component {
    ALIEN,
    HUMAN,
    PIRATE,
    ROBOT;

    operator fun next(): FractionsType {
        return vals[(this.ordinal + 1) % vals.size]
    }

    companion object {

        private val vals = values()
    }
}

