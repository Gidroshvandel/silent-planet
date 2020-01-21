package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.viewport.Viewport
import com.silentgames.core.logic.Constants
import com.silentgames.graphic.mvp.InputMultiplexer

class Hud(val viewport: Viewport) {

    val stage = Stage()

    var image: Image = Image()

    fun dispose() {
        stage.dispose()
    }

    val stageLayout = Table()

    val atlas = TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"))
    val skin = Skin(Gdx.files.internal("ui/uiskin.json"), atlas)
    val list = List<String>(skin).apply {
        //        setItems(*listOf("YES", "YES").toTypedArray())
    }
    val scrollPane = ScrollPane(list)

    init {
//        val strings = arrayOfNulls<String>(200)
//        var i = 0
//        var k = 0
//        while (i < strings.size) {
//            strings[k++] = "String: $i"
//            i++
//        }
//        list.setItems(*strings)
//        scrollPane.setBounds(0, 0, gameWidth, gameHeight + 100);
//        scrollPane.setSmoothScrolling(false);
//        scrollPane.setPosition(gameWidth / 2 - scrollPane.getWidth() / 4,
//                gameHeight / 2 - scrollPane.getHeight() / 4);
        stage.addActor(stageLayout.apply {
            // добавление таблицы в сцену
            debugAll() // Включаем дебаг для всех элементов таблицы
            setFillParent(true) // Указываем что таблица принимает размеры родителя
            padLeft(viewport.screenHeight.toFloat())
            row().let {
                //                add(Image(Texture("dead_cell.png"))).top().right()
//                add(GameActor(viewport))
//                add(Image(Texture("dead_cell.png")))
                add(image).top()
                add(scrollPane).top()
//                add(Image(Texture("dead_cell.png"))).fill()
            }
        })
        InputMultiplexer.addProcessor(stage)
    }

    fun update(entityList: kotlin.collections.List<EntityData>, onClick: (EntityData) -> Unit) {
        list.setItems(*entityList.map { it.name }.toTypedArray())
        list.addListener { event ->
            if (event is InputEvent && event.type == InputEvent.Type.touchDown) {
                entityList.find { it.name == list.selected }?.let(onClick)
            }
            return@addListener false
        }
        scrollPane.invalidateHierarchy()
    }

    fun updateImage(imagePath: String) {

        val size = (Gdx.graphics.height / Constants.verticalCountOfCells).toFloat()

        val sprite = Sprite(Texture(imagePath))

        sprite.setSize(size, size)

        image.drawable = SpriteDrawable(sprite)
    }

}