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
    override val id: Int get() = name.hashCode() + description.hashCode()

    fun addEffect(effect: Effect) {
        effects.add(effect)
    }

    fun removeEffect(effect: Effect) {
        effects.remove(effect)
    }

    inline fun <reified T : Effect> hasEffect() = getEffect<T>() != null

    inline fun <reified T : Effect> getEffect() = effects.filterIsInstance<T>().firstOrNull()
}
