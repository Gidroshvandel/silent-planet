package com.silentgames.core.logic.ecs.entity.cell.crystal

import kotlin.random.Random


enum class CrystalsEnum(val crystalsCount: Int) {

    ONE(1) {
        override fun getImageName(): String = "one_crystal.png"
    },
    TWO(2) {
        override fun getImageName(): String = "two_crystals.png"
    },
    THREE(3) {
        override fun getImageName(): String = "three_crystals.png"
    };

    abstract fun getImageName(): String

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random
        fun random(): CrystalsEnum {
            return values[random.nextInt(size)]
        }
    }


}