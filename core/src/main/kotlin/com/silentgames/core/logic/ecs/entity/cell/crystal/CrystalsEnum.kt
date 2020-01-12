package com.silentgames.core.logic.ecs.entity.cell.crystal


import java.util.*

enum class CrystalsEnum(val crystalsCount: Int) {

    ONE(1) {
        override fun getImageId(): String = "one_crystal"
    },
    TWO(2) {
        override fun getImageId(): String = "two_crystals"
    },
    THREE(3) {
        override fun getImageId(): String = "three_crystals"
    };

    abstract fun getImageId(): String

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): CrystalsEnum {
            return values[random.nextInt(size)]
        }
    }


}