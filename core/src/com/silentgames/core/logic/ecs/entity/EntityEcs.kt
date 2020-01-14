package com.silentgames.core.logic.ecs.entity

import com.silentgames.core.logic.ecs.component.Component
import java.io.Serializable

open class EntityEcs : Serializable {

    val id = EntityIdGenerator.generateId()

    @PublishedApi
    internal val components: Set<Component>
        get() = localComponents

    private var localComponents: Set<Component> = setOf()

    inline fun <reified T : Component> getComponent(): T? {
        return components.filterIsInstance<T>().firstOrNull()
    }

    inline fun <reified T : Component> getMandatoryComponent(): T {
        return components.filterIsInstance<T>().first()
    }

    inline fun <reified T : Component> hasComponent(): Boolean = getComponent<T>() != null

    fun addComponent(component: Component) {
        if (components.contains(component)) {
            removeComponent(component)
        }
        localComponents = localComponents.toMutableSet().apply {
            add(component)
        }
    }

    fun removeComponent(component: Component) {
        localComponents = localComponents.toMutableSet().apply {
            remove(component)
        }
    }

    fun <T : Component> removeComponent(clazz: Class<T>) {
        components.filterIsInstance(clazz).firstOrNull()?.let {
            removeComponent(it)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is EntityEcs) {
            other.id == id
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}