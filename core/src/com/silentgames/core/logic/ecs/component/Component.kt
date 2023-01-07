package com.silentgames.core.logic.ecs.component

interface Component

abstract class ComponentEquals : Component {

    override fun equals(other: Any?): Boolean {
        return if (other is ComponentEquals) {
            other.javaClass.name == this.javaClass.name
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return this.javaClass.name.hashCode()
    }
}
