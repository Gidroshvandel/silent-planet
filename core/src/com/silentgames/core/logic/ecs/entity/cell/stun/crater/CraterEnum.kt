package com.silentgames.core.logic.ecs.entity.cell.stun.crater

import java.util.*

enum class CraterEnum(val turnCount: Int) {

    ONE(1) {
        override fun getImageName(): String = "crater_one"
    },
    TWO(2) {
        override fun getImageName(): String = "crater_two"
    },
    THREE(3) {
        override fun getImageName(): String = "crater_three"
    },
    FOUR(4) {
        override fun getImageName(): String = "crater_four"
    };

    abstract fun getImageName(): String

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): CraterEnum {
            return values[random.nextInt(size)]
        }
    }
}
