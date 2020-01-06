package com.silentgames.silent_planet.logic.ecs.component

interface Component {

    val id: String get() = this.javaClass.name

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