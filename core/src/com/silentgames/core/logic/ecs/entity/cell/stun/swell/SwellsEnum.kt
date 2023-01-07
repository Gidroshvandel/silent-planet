package com.silentgames.core.logic.ecs.entity.cell.stun.swell

import java.util.*

enum class SwellsEnum(val turnCount: Int) {

    ONE(1) {
        override fun getImageName(): String = "swell_one"
    },
    TWO(2) {
        override fun getImageName(): String = "swell_two"
    },
    THREE(3) {
        override fun getImageName(): String = "swell_three"
    },
    FOUR(4) {
        override fun getImageName(): String = "swell_four"
    };

    abstract fun getImageName(): String

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): SwellsEnum {
            return values[random.nextInt(size)]
        }
    }
}
