package com.silentgames.core.logic.ecs.entity.cell.stun.disease


import java.util.*

enum class DiseaseEnum(val turnCount: Int) {

    TWO(2) {
        override fun getImageName(): String = "disease_two"
    };

    abstract fun getImageName(): String

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): DiseaseEnum {
            return values[random.nextInt(size)]
        }
    }


}