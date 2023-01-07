package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.silentgames.core.Strings
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.Assets
import com.silentgames.graphic.setTextColor

class TopScorePanel(private val uiSkin: Skin) : Table() {

    private val humansButton = createTextButton(getCrystalTitle(Strings.humans.getString(), 0))
    private val piratesButton = createTextButton(getCrystalTitle(Strings.pirates.getString(), 0))
    private val robotsButton = createTextButton(getCrystalTitle(Strings.robots.getString(), 0))
    private val aliensButton = createTextButton(getCrystalTitle(Strings.aliens.getString(), 0))

    var onHumansClick: (() -> Unit)? = null
        set(value) {
            humansButton.addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    var onPiratesClick: (() -> Unit)? = null
        set(value) {
            piratesButton.addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    var onRobotsClick: (() -> Unit)? = null
        set(value) {
            robotsButton.addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    var onAliensClick: (() -> Unit)? = null
        set(value) {
            aliensButton.addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    private fun createTextButton(text: String) = TextButton(text, uiSkin, "rounded_window")

    init {
        val height = 56f
        add(humansButton).pad(1f).height(height).grow()
        add(piratesButton).pad(1f).height(height).grow()
        add(robotsButton).pad(1f).height(height).grow()
        add(aliensButton).pad(1f).height(height).grow()
    }

    fun changeFractionCrystalOnBoard(fractionsType: FractionsType, count: Int) {
        when (fractionsType) {
            FractionsType.ALIEN -> aliensButton.setText(
                getCrystalTitle(
                    Strings.aliens.getString(),
                    count
                )
            )
            FractionsType.HUMAN -> humansButton.setText(
                getCrystalTitle(
                    Strings.humans.getString(),
                    count
                )
            )
            FractionsType.PIRATE -> piratesButton.setText(
                getCrystalTitle(
                    Strings.pirates.getString(),
                    count
                )
            )
            FractionsType.ROBOT -> robotsButton.setText(
                getCrystalTitle(
                    Strings.robots.getString(),
                    count
                )
            )
        }
    }

    fun selectFraction(fractionsType: FractionsType) {
        aliensButton.setColor(Assets.TextColor.WHITE)
        humansButton.setColor(Assets.TextColor.WHITE)
        piratesButton.setColor(Assets.TextColor.WHITE)
        robotsButton.setColor(Assets.TextColor.WHITE)
        when (fractionsType) {
            FractionsType.ALIEN -> aliensButton.setColor(Assets.TextColor.RED)
            FractionsType.HUMAN -> humansButton.setColor(Assets.TextColor.RED)
            FractionsType.PIRATE -> piratesButton.setColor(Assets.TextColor.RED)
            FractionsType.ROBOT -> robotsButton.setColor(Assets.TextColor.RED)
        }
    }

    private fun TextButton.setColor(color: Assets.TextColor) {
        this.setTextColor(color, uiSkin)
    }

    private fun getCrystalTitle(fractionName: String, currentCrystals: Int): String {
        return Strings.crystal_count.getString(
            fractionName,
            currentCrystals,
            Constants.countCrystalsToWin
        )
    }
}
