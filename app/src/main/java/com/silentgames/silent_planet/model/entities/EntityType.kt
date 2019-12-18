package com.silentgames.silent_planet.model.entities

import android.content.Context
import com.silentgames.silent_planet.model.effects.Effect
import com.silentgames.silent_planet.model.fractions.Fractions

/**
 * Created by gidroshvandel on 09.07.16.
 */

abstract class EntityType(
        val context: Context,
        override var fraction: Fractions,
        override var crystals: Int = 0,
        override var isDead: Boolean = false,
        val effects: MutableList<Effect> = mutableListOf()
) : EntityTypeProperties {

    override val id: String get() = name + "_" + description + "_" + fraction.fractionsType.name

    fun addEffect(effect: Effect) {
        effects.add(effect)
    }

    fun removeEffect(effect: Effect) {
        effects.remove(effect)
    }

    fun addCrystals(crystals: Int) {
        this.crystals = this.crystals + crystals
    }

    fun removeCrystals(crystals: Int) {
        val result = this.crystals - crystals
        this.crystals = if (result >= 0) result else 0
    }

    inline fun <reified T : Effect> hasEffect() = getEffect<T>() != null

    inline fun <reified T : Effect> getEffect() = effects.filterIsInstance<T>().firstOrNull()

    override fun equals(other: Any?): Boolean {
        return if (other is EntityType) {
            other.id == id
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + fraction.fractionsType.hashCode()
        return result
    }
}
