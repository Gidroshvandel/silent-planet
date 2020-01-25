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
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.silentgames.core.Strings
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.FractionsType.*
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.mvp.main.Hud.Color.RED
import com.silentgames.graphic.mvp.main.Hud.Color.WHITE

class Hud {

    val stage = Stage(ScreenViewport())

    fun dispose() {
        stage.dispose()
    }

    private val table = Table()

    private val uiSkin = Assets().uiSkin

    private val atlas = TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"))
    private val testSkin = Skin(Gdx.files.internal("ui/uiskin.json"), atlas)
    private val list = List<String>(testSkin)
    private val scrollPane = ScrollPane(list)

    private val humansLabel = Label(getCrystalTitle(Strings.humans.getString(), 0), uiSkin)
    private val piratesLabel = Label(getCrystalTitle(Strings.pirates.getString(), 0), uiSkin)
    private val robotsLabel = Label(getCrystalTitle(Strings.robots.getString(), 0), uiSkin)
    private val aliensLabel = Label(getCrystalTitle(Strings.aliens.getString(), 0), uiSkin)

    private val tableGen = Table()

    init {
        stage.addActor(
                tableGen.apply {
                    setFillParent(true)
                    debugAll()
                    add().size(Gdx.graphics.height.toFloat(), Gdx.graphics.height.toFloat())
                    add(table.apply {
                        add(Table().apply {
                            debugAll()
                            row().let {
                                add(humansLabel)
                                add(piratesLabel)
                                add(robotsLabel)
                                add(aliensLabel)
                            }
                        })
                    }).expand().top().right()
                }
        )
        table.debugAll()
        InputMultiplexer.addProcessor(stage)
    }

    fun addWidget(entityData: EntityData) {
        table.apply {
            row().apply {
                add(Table().apply {
                    debugAll()
                    row().apply {
                        add(Image().apply { setTexture(entityData.texture) })
                        add(Label(entityData.description, uiSkin))
                        add(Image().apply { setTexture("crystal.png") }).center()
                    }
                    row().apply {
                        add(Label(entityData.name, uiSkin))
                    }
                })
            }
        }
    }

    fun update(height: Int) {
        val heightCell = height.toFloat()
        tableGen.cells.first().size(heightCell, heightCell)
    }

    private fun Image.setTexture(path: String) {
        val size = (Gdx.graphics.height / Constants.verticalCountOfCells).toFloat()
        val sprite = Sprite(Texture(path))
        sprite.setSize(size, size)
        this.drawable = SpriteDrawable(sprite)
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