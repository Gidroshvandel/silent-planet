package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Scaling
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.AppViewport
import com.silentgames.graphic.Assets


class SilentPlanetGame : ApplicationAdapter(), SilentPlanetContract.View {

    private lateinit var presenter: SilentPlanetContract.Presenter

    private val viewPort = AppViewport(Scaling.fillY, HEIGHT, HEIGHT)
    private val camera by lazy(viewPort::getCamera)

    private val assets by lazy { Assets() }

    private val hud by lazy { Hud(viewPort, assets) }

    override fun create() {

        presenter = SilentPlanetPresenter(
                this,
                null,
                SilentPlanetViewModel(),
                SilentPlanetModel(viewPort)
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
    }

    override fun render() {

        presenter.onRender()

        hud.stage.viewport.apply(true)

        hud.drawBackground()
        hud.stage.act()
        hud.stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewPort.update(width, height)

        hud.stage.viewport.update(width, height, true)

        hud.stage.act()
        hud.stage.draw()
    }

    override fun dispose() {
//        engine.dispose()
    }

    companion object {
        const val WIDTH = 640f
        const val HEIGHT = 480f
    }

    override fun showEntityInfo(entity: EntityData) {
        hud.addWidget(entity)
    }

    override fun showToast(text: String) {
    }

    override fun enableButton(isEnabled: Boolean) {
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
    }

    override fun showPlayerBuybackFailureMessage(missingAmount: Int) {
    }
}