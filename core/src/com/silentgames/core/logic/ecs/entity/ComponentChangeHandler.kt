package com.silentgames.core.logic.ecs.entity

import com.silentgames.core.logic.ecs.component.Component

class ComponentChangeHandler {

    @Transient
    val onComponentChangedList: MutableMap<Class<*>, MutableList<(ChangeHandler)>> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Component> addComponentChangedListener(order: Int, noinline onChanged: (T) -> Unit) {
        val changeHandler = ChangeHandler(onChanged as ((Component) -> Unit), order)
        if (onComponentChangedList[T::class.java] == null) {
            onComponentChangedList[T::class.java] = mutableListOf()
        }
        onComponentChangedList[T::class.java]?.add(changeHandler)
        onComponentChangedList[T::class.java]?.sortBy { it.order }
    }

    inline fun <reified T : Component> addComponentChangedListener(noinline onChanged: (T) -> Unit) {
        addComponentChangedListener(Int.MAX_VALUE, onChanged)
    }

    fun addComponent(component: Component) {
        onComponentChangedList.forEach { map ->
            if (map.key == component::class.java) {
                map.value.forEach {
                    it.onComponentAdd.invoke(component)
                }
            }
        }
    }

    data class ChangeHandler(val onComponentAdd: (Component) -> Unit, val order: Int)

}