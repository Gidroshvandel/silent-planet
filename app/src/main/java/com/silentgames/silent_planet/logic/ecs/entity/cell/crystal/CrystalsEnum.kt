package com.silentgames.silent_planet.logic.ecs.entity.cell.crystal

import com.silentgames.silent_planet.R
import java.util.*

enum class CrystalsEnum(val crystalsCount: Int) {

    ONE(1) {
        override fun getImageId(): Int = R.drawable.one_crystal
    },
    TWO(2) {
        override fun getImageId(): Int = R.drawable.two_crystals
    },
    THREE(3) {
        override fun getImageId(): Int = R.drawable.three_crystals
    };

    abstract fun getImageId(): Int

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): CrystalsEnum {
            return values[random.nextInt(size)]
        }
    }


}