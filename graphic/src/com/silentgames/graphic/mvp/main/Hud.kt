package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.Viewport
import com.silentgames.core.Strings
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.FractionsType.*
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.mvp.main.Hud.Color.RED
import com.silentgames.graphic.mvp.main.Hud.Color.WHITE
import ktx.style.get

class Hud(gameViewport: Viewport, private val assets: Assets) {

    private val uiSkin by lazy { assets.uiSkin }

    private val roundedWindow by lazy { NinePatchDrawable(uiSkin.get<NinePatch>("ui/rounded_window")) }
    private val customWindow by lazy { NinePatchDrawable(uiSkin.get<NinePatch>("ui/custom_window")) }
    private val background by lazy { uiSkin.get<Sprite>("space_texture") }

    val stage = Stage(
            object : Viewport() {

                init {
                    camera = OrthographicCamera()
                }

                override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
                    val freeSpaceWidth = screenWidth - gameViewport.screenWidth

                    setScreenBounds(gameViewport.screenWidth, 0, freeSpaceWidth, screenHeight)
                    setWorldSize(freeSpaceWidth.toFloat(), screenHeight.toFloat())
                    apply(centerCamera)
                }
            }
    )

    fun dispose() {
        stage.dispose()
    }

    private val table = Table()

    private val humansLabel = createLabel(getCrystalTitle(Strings.humans.getString(), 0))
    private val piratesLabel = createLabel(getCrystalTitle(Strings.pirates.getString(), 0))
    private val robotsLabel = createLabel(getCrystalTitle(Strings.robots.getString(), 0))
    private val aliensLabel = createLabel(getCrystalTitle(Strings.aliens.getString(), 0))

    private fun createLabel(text: String) =
            Label(text, uiSkin, "large").apply {
                setAlignment(Align.center)
            }

    private fun Table.addWithPadding(actor: Actor, padding: Float, background: Drawable): Cell<Table> =
            add(actor.addPadding(padding).addWindowBackground(background)).grow().height(actor.height + padding)

    private fun Actor.addPadding(padding: Float = 0f): Table =
            Table().also { table ->
                table.add(this).grow().pad(padding)
            }


    private fun Table.addWindowBackground(background: Drawable): Table = apply {
        this.background = background
    }

    init {
        stage.addActor(
                Table().apply {
                    setFillParent(true)
                    pad(20f, 20f, 0f, 20f)
                    this.top()
                    add(Table().apply {
                        row().expandX().let {
                            addWithPadding(humansLabel, 4f, roundedWindow).pad(1f)
                            addWithPadding(piratesLabel, 4f, roundedWindow).pad(1f)
                            addWithPadding(robotsLabel, 4f, roundedWindow).pad(1f)
                            addWithPadding(aliensLabel, 4f, roundedWindow).pad(1f)
                        }
                    }).growX()
                    row().grow()
                    add(ScrollPane(table))
                })
        InputMultiplexer.addProcessor(stage)
    }

    fun drawBackground() {
        stage.batch.begin()
        stage.batch.disableBlending()
        stage.batch.draw(background, 0f, 0f, stage.viewport.worldWidth, stage.viewport.worldHeight)
        stage.batch.enableBlending()
        stage.batch.end()
    }

    fun addWidget(entityData: EntityData) {
        update(listOf(entityData)) {}
    }

    private fun Table.addWidget(entityData: EntityData) {
        this.apply {
            add(Table().also {
                it.row()
                it.add(Image().apply { setTexture(entityData.texture) }).pad(5f)
                it.row()
                it.add(
                        Label(entityData.name, uiSkin).also { label ->
                            label.setAlignment(Align.center)
                        }
                ).pad(5f)
            }).space(5f)
            add(
                    Label(entityData.description, uiSkin).also {
                        it.setWrap(true)
                        it.setAlignment(Align.center)
                    }).prefWidth(150f).growX().space(5f)
            add(Image().apply { setTexture("crystal.png") }).space(5f).center()
        }
    }

    private fun Image.setTexture(path: String) {
        val size = (SilentPlanetGame.HEIGHT / Constants.verticalCountOfCells)
        val sprite = assets.getSprite(path)
        sprite.setSize(size, size)
        this.drawable = SpriteDrawable(sprite)
    }

    private fun getCrystalTitle(fractionName: String, currentCrystals: Int): String {
        return Strings.crystal_count.getString(fractionName, currentCrystals, Constants.countCrystalsToWin)
    }

    fun update(entityList: List<EntityData>, onClick: (EntityData) -> Unit) {
        table.clear()
        entityList.forEach { entityData ->
            table.row().growX()
            table.add(Table().apply {
                background = customWindow
                pad(15f, 35f, 15f, 45f)
                addWidget(entityData)
                addListener { event ->
                    if (event is InputEvent && event.type == InputEvent.Type.touchDown) {
                        onClick(entityData)
                        return@addListener true
                    }
                    return@addListener false
                }
            })
        }
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
        this.style = LabelStyle(uiSkin.getFont(Font.REGULAR.fontName), uiSkin.getColor(color.colorName)).also {
            it.background = this.style.background
        }
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