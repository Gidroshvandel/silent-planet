package com.silentgames.silent_planet.logic.ecs.entity

import com.silentgames.silent_planet.logic.ecs.component.Component

open class Entity {

    val components: MutableList<Component> = mutableListOf()

    inline fun <reified T : Component> getComponent(): T? {
        return components.filterIsInstance<T>().firstOrNull()
    }

}