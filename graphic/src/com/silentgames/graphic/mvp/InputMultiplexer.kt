package com.silentgames.graphic.mvp

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor

object InputMultiplexer {

    private val inputMultiplexer = InputMultiplexer()

    fun addProcessor(processor: InputProcessor) {
        inputMultiplexer.addProcessor(processor)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    fun clear() {
        inputMultiplexer.clear()
    }
}