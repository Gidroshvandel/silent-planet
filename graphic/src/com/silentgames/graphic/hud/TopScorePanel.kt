package com.silentgames.graphic.hud

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Align
import com.silentgames.core.Strings
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.setColor
import ktx.style.get

class TopScorePanel(private val uiSkin: Skin) : Table() {

    private val roundedWindow by lazy { NinePatchDrawable(uiSkin.get<NinePatch>("ui/rounded_window_blue")) }

    private val humansLabel = createLabel(getCrystalTitle(Strings.humans.getString(), 0))
    private val piratesLabel = createLabel(getCrystalTitle(Strings.pirates.getString(), 0))
    private val robotsLabel = createLabel(getCrystalTitle(Strings.robots.getString(), 0))
    private val aliensLabel = createLabel(getCrystalTitle(Strings.aliens.getString(), 0))

    private fun createLabel(text: String) =
            Label(text, uiSkin, "large").apply {
                setAlignment(Align.center)
            }

    private fun Table.addWithPadding(actor: Actor, padding: Float, background: Drawable): Cell<Table> =
            add(actor.addPadding(padding).addWindowBackground(background))
                    .grow()
                    .height(actor.height + padding * 2)

    private fun Actor.addPadding(padding: Float = 0f): Table =
            Table().also { table ->
                table.add(this).grow().pad(padding)
            }

    private fun Table.addWindowBackground(background: Drawable): Table = apply {
        this.background = background
    }

    init {
        row().expandX().let {
            addWithPadding(humansLabel, 4f, roundedWindow).pad(1f)
            addWithPadding(piratesLabel, 4f, roundedWindow).pad(1f)
            addWithPadding(robotsLabel, 4f, roundedWindow).pad(1f)
            addWithPadding(aliensLabel, 4f, roundedWindow).pad(1f)
        }
    }

    fun changeFractionCrystalOnBoard(fractionsType: FractionsType, count: Int) {
        when (fractionsType) {
            FractionsType.ALIEN -> aliensLabel.setText(getCrystalTitle(Strings.aliens.getString(), count))
            FractionsType.HUMAN -> humansLabel.setText(getCrystalTitle(Strings.humans.getString(), count))
            FractionsType.PIRATE -> piratesLabel.setText(getCrystalTitle(Strings.pirates.getString(), count))
            FractionsType.ROBOT -> robotsLabel.setText(getCrystalTitle(Strings.robots.getString(), count))
        }
    }

    fun selectFraction(fractionsType: FractionsType) {
        aliensLabel.setColor(Hud.Color.WHITE)
        humansLabel.setColor(Hud.Color.WHITE)
        piratesLabel.setColor(Hud.Color.WHITE)
        robotsLabel.setColor(Hud.Color.WHITE)
        when (fractionsType) {
            FractionsType.ALIEN -> aliensLabel.setColor(Hud.Color.RED)
            FractionsType.HUMAN -> humansLabel.setColor(Hud.Color.RED)
            FractionsType.PIRATE -> piratesLabel.setColor(Hud.Color.RED)
            FractionsType.ROBOT -> robotsLabel.setColor(Hud.Color.RED)
        }
    }

    private fun Label.setColor(color: Hud.Color) {
        this.setColor(color, uiSkin)
    }

    private fun getCrystalTitle(fractionName: String, currentCrystals: Int): String {
        return Strings.crystal_count.getString(fractionName, currentCrystals, Constants.countCrystalsToWin)
    }

}