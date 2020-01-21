package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.silentgames.core.Strings
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.FractionsType.*
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.mvp.main.Hud.Color.RED
import com.silentgames.graphic.mvp.main.Hud.Color.WHITE

class Hud {

    val stage = Stage()

    private var image: Image = Image()

    fun dispose() {
        stage.dispose()
    }

    private val stageLayout = Table()

    private val uiSkin = Assets().uiSkin

    private val atlas = TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"))
    private val skin = Skin(Gdx.files.internal("ui/uiskin.json"), atlas)
    private val list = List<String>(skin)
    private val scrollPane = ScrollPane(list)

    private val humansLabel = Label(getCrystalTitle(Strings.humans.getString(), 0), uiSkin)
    private val piratesLabel = Label(getCrystalTitle(Strings.pirates.getString(), 0), uiSkin)
    private val robotsLabel = Label(getCrystalTitle(Strings.robots.getString(), 0), uiSkin)
    private val aliensLabel = Label(getCrystalTitle(Strings.aliens.getString(), 0), uiSkin)

    init {
        stage.addActor(stageLayout.right().top().apply {
            row().let {
                add(humansLabel)
                add(piratesLabel)
                add(robotsLabel)
                add(aliensLabel)
            }.top().right()
            debugAll() // Включаем дебаг для всех элементов таблицы
            setFillParent(true) // Указываем что таблица принимает размеры родителя
            row().let {
                add(image).top()
                add(scrollPane).top()
            }
        })
        InputMultiplexer.addProcessor(stage)
    }

    private fun getCrystalTitle(fractionName: String, currentCrystals: Int): String {
        return Strings.crystal_count.getString(fractionName, currentCrystals, Constants.countCrystalsToWin)
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

    fun changeFractionCrystalOnBoard(fractionsType: FractionsType, count: Int) {
        when (fractionsType) {
            ALIEN -> aliensLabel.setText(getCrystalTitle(Strings.aliens.getString(), count))
            HUMAN -> humansLabel.setText(getCrystalTitle(Strings.humans.getString(), count))
            PIRATE -> piratesLabel.setText(getCrystalTitle(Strings.pirates.getString(), count))
            ROBOT -> robotsLabel.setText(getCrystalTitle(Strings.robots.getString(), count))
        }
    }

    fun selectFraction(fractionsType: FractionsType) {
        aliensLabel.setColor(WHITE)
        humansLabel.setColor(WHITE)
        piratesLabel.setColor(WHITE)
        robotsLabel.setColor(WHITE)
        when (fractionsType) {
            ALIEN -> aliensLabel.setColor(RED)
            HUMAN -> humansLabel.setColor(RED)
            PIRATE -> piratesLabel.setColor(RED)
            ROBOT -> robotsLabel.setColor(RED)
        }
    }

    private fun Label.setColor(color: Color) {
        this.style = LabelStyle(uiSkin.getFont(Font.REGULAR.fontName), uiSkin.getColor(color.colorName))
    }

    enum class Color(val colorName: String) {
        WHITE("white"),
        RED("red")
    }

    enum class Font(val fontName: String) {
        SMALL("small-font"),
        LARGE("large-font"),
        REGULAR("regular-font")
    }

}