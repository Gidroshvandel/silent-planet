package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.AppViewport
import com.silentgames.graphic.Assets
import com.silentgames.graphic.Logger
import com.silentgames.graphic.hud.Hud
import com.silentgames.graphic.hud.Toast
import com.silentgames.graphic.hud.Toast.ToastFactory
import com.silentgames.graphic.manager.game.GameManager


class SilentPlanetGame : ApplicationAdapter(), SilentPlanetContract.View {

    private lateinit var presenter: SilentPlanetContract.Presenter

    private val fullViewPort = ScreenViewport()
    private val viewPort = AppViewport(Scaling.fillY, HEIGHT, HEIGHT)
    private val camera by lazy(viewPort::getCamera)

    private val assets by lazy { Assets() }

    private val hud by lazy { Hud(viewPort, assets) }

    private val toastFactory by lazy { ToastFactory.Builder().font(assets.uiSkin.getFont(Assets.Font.REGULAR.fontName)).build() }

    private var currentToast: Toast? = null

    override fun create() {

        Logger.create()

        presenter = SilentPlanetPresenter(
                this,
                GameManager.loadData(),
                SilentPlanetViewModel(),
                SilentPlanetModel(viewPort, assets)
        )

        initUi()

        presenter.onCreate()
    }

    private fun initUi() {
//        Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)

        (camera as? OrthographicCamera)?.zoom = 1f
        (camera as? OrthographicCamera)?.setToOrtho(true)
        camera.position.x = HEIGHT / 2f
        camera.position.y = HEIGHT / 2f

        hud.onGetCrystalClick {
            presenter.onActionButtonClick()
        }

        hud.onSkipTurnClick {
            presenter.onTurnSkipped()
        }

        hud.onHumansClick {
            presenter.onTopScorePanelClick(FractionsType.HUMAN)
        }

        hud.onPiratesClick {
            presenter.onTopScorePanelClick(FractionsType.PIRATE)
        }

        hud.onRobotsClick {
            presenter.onTopScorePanelClick(FractionsType.ROBOT)
        }

        hud.onAliensClick {
            presenter.onTopScorePanelClick(FractionsType.ALIEN)
        }
    }

    override fun render() {
//        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        presenter.onRender()

        hud.stage.viewport.apply(true)

        hud.drawBackground()
        hud.stage.act()
        hud.stage.draw()

        fullViewPort.apply(true)

        currentToast?.render(fullViewPort)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewPort.update(width, height)

        hud.stage.viewport.update(width, height, true)

        hud.stage.act()
        hud.stage.draw()

        fullViewPort.update(width, height, true)
        currentToast?.update(width.toFloat())
    }

    override fun pause() {
        presenter.saveInstanceState {
            GameManager.saveData(it)
        }
    }

    override fun dispose() {
//        engine.dispose()
    }

    companion object {
        const val WIDTH = 640f
        const val HEIGHT = 480f
    }

    override fun showToast(text: String) {
        currentToast = toastFactory.create(text, Toast.Length.LONG)
    }

    override fun enableCrystalActionButton(isEnabled: Boolean) {
        hud.setCrystalActionButtonEnabled(isEnabled)
    }

    override fun enableSkipTurnButton(isEnabled: Boolean) {
        hud.setSkipTurnButtonEnabled(isEnabled)
    }

    override fun changeBottomActionButtonVisibility(visible: Boolean) {
        hud.setBottomActionPanelVisibility(visible)
    }

    override fun showEntityMenuDialog(dataList: MutableList<EntityData>) {
        hud.update(dataList) {
            presenter.onEntityDialogElementSelect(it)
        }
    }

    override fun changeAlienCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.ALIEN, crystals)
    }

    override fun changeHumanCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.HUMAN, crystals)
    }

    override fun changePirateCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.PIRATE, crystals)
    }

    override fun changeRobotCristalCount(crystals: Int) {
        hud.changeFractionCrystalOnBoard(FractionsType.ROBOT, crystals)
    }

    override fun selectCurrentFraction(fractionType: FractionsType) {
        hud.selectFraction(fractionType)
    }

    override fun showPlayerBuybackSuccessMessage(name: String) {
        currentToast = toastFactory.create(Strings.player_buyback_success.getString(name), Toast.Length.LONG)
    }

    override fun showPlayerBuybackFailureMessage(missingAmount: Int) {
        currentToast = toastFactory.create(Strings.player_buyback_failure.getString(missingAmount), Toast.Length.LONG)
    }
}