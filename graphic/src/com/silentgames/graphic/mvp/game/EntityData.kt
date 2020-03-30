package com.silentgames.graphic.mvp.game

class EntityData(
        val id: Long,
        val texture: String,
        val name: String,
        val description: String,
        val crystalCount: String,
        val captured: Boolean = false
)