package com.silentgames.core.logic.ecs.entity.cell.stun.meteorites

import java.util.*

enum class MeteoritesEnum(val turnCount: Int) {

    TWO(2) {
        override fun getImageName(): String = "meteorites_two"
    };

    abstract fun getImageName(): String

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): MeteoritesEnum {
            return values[random.nextInt(size)]
        }
    }
}
