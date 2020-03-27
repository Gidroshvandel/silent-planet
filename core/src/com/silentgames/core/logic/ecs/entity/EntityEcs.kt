package com.silentgames.core.logic.ecs.entity

import com.silentgames.core.logic.ecs.component.Component

abstract class EntityEcs(
        val id: Long = EntityIdGenerator.generateId(),
        private var localComponents: Set<Component> = setOf()
) {

    @PublishedApi
    internal val components: Set<Component>
        get() = localComponents

    inline fun <reified T : Component> getComponent(): T? {
        return components.filterIsInstance<T>().firstOrNull()
    }

    inline fun <reified T : Component> getMandatoryComponent(): T {
        return components.filterIsInstance<T>().first()
    }

    inline fun <reified T : Component> hasComponent(): Boolean = getComponent<T>() != null

    val componentChangeHandler = ComponentChangeHandler()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Component> addComponentChangedListener(noinline onChanged: (T) -> Unit) {
        componentChangeHandler.addComponentChangedListener(onChanged)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Component> addComponentChangedListener(order: Int, noinline onChanged: (T) -> Unit) {
        componentChangeHandler.addComponentChangedListener(order, onChanged)
    }

    fun addComponent(component: Component) {

        componentChangeHandler.addComponent(component)

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