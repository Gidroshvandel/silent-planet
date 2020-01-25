package com.silentgames.core.logic.ecs.component

interface Component {

    val id: String get() = this::class.simpleName ?: ""

}

abstract class ComponentEquals : Component {

    override fun equals(other: Any?): Boolean {
        return if (other is Component) {
            other.id == this.id
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}