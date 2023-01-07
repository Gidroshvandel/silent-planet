package com.silentgames.core.logic.ecs.entity

object EntityIdGenerator {

    private var id: Long = 0

    fun generateId() = id++
}
