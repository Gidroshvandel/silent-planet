package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.silentgames.core.Strings
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.Assets
import com.silentgames.graphic.setTextColor

class TopScorePanel(private val uiSkin: Skin) : Table() {

    private val humansLabel = createTextButton(getCrystalTitle(Strings.humans.getString(), 0))
    private val piratesLabel = createTextButton(getCrystalTitle(Strings.pirates.getString(), 0))
    private val robotsLabel = createTextButton(getCrystalTitle(Strings.robots.getString(), 0))
    private val aliensLabel = createTextButton(getCrystalTitle(Strings.aliens.getString(), 0))

    private fun createTextButton(text: String) = TextButton(text, uiSkin, "rounded_window")

    init {
        val height = 56f
        add(humansLabel).pad(1f).height(height).grow()
        add(piratesLabel).pad(1f).height(height).grow()
        add(robotsLabel).pad(1f).height(height).grow()
        add(aliensLabel).pad(1f).height(height).grow()
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
        aliensLabel.setColor(Assets.TextColor.WHITE)
        humansLabel.setColor(Assets.TextColor.WHITE)
        piratesLabel.setColor(Assets.TextColor.WHITE)
        robotsLabel.setColor(Assets.TextColor.WHITE)
        when (fractionsType) {
            FractionsType.ALIEN -> aliensLabel.setColor(Assets.TextColor.RED)
            FractionsType.HUMAN -> humansLabel.setColor(Assets.TextColor.RED)
            FractionsType.PIRATE -> piratesLabel.setColor(Assets.TextColor.RED)
            FractionsType.ROBOT -> robotsLabel.setColor(Assets.TextColor.RED)
        }
    }

    private fun TextButton.setColor(color: Assets.TextColor) {
        this.setTextColor(color, uiSkin)
    }

    private fun getCrystalTitle(fractionName: String, currentCrystals: Int): String {
        return Strings.crystal_count.getString(fractionName, currentCrystals, Constants.countCrystalsToWin)
    }

}