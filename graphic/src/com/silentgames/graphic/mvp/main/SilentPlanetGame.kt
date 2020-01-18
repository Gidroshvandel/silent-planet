package com.silentgames.graphic.mvp.main

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.silentgames.core.logic.CellRandomGenerator
import com.silentgames.core.logic.EntityRandomGenerator
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.InputMouse

class SilentPlanetGame : ApplicationAdapter(), SilentPlanetContract.View {

    private lateinit var presenter: SilentPlanetContract.Presenter

    private val screenRect = Rectangle(0f, 0f, WIDTH * 2, HEIGHT * 2)
    private val viewPort = FitViewport(WIDTH, HEIGHT)
    private val camera by lazy(viewPort::getCamera)

    override fun create() {

        presenter = SilentPlanetPresenter(
                this,
                null,
                SilentPlanetViewModel(),
                SilentPlanetModel(camera)
        )

        initUi()

        presenter.onCreate()
    }

    private fun initUi() {
        (camera as? OrthographicCamera)?.zoom = 1f
        camera.position.x = WIDTH / 2f
        camera.position.y = HEIGHT / 2f

        Gdx.input.inputProcessor = InputMouse()
    }

    override fun render() {
        Gdx.gl.glClearColor(0.5f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        presenter.onRender()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewPort.update(width, height)
        screenRect.width = viewPort.worldWidth
        screenRect.height = viewPort.worldHeight
    }

    override fun dispose() {
//        engine.dispose()
    }

    fun generateNewBattleGround(firstTurnFraction: FractionsType): GameState = GameState(
            CellRandomGenerator().generateBattleGround(),
            EntityRandomGenerator().generateUnits(),
            firstTurnFraction
    )

    companion object {
        const val WIDTH = 640f
        const val HEIGHT = 640f
    }

    override fun showToast(text: String) {
    }

    override fun fillEntityName(text: String) {
    }

    override fun fillDescription(text: String) {
    }

    override fun showObjectIcon(bitmap: String) {
    }

    override fun enableButton(isEnabled: Boolean) {
    }

    override fun setImageCrystalText(text: String) {
    }

    override fun changeAlienCristalCount(crystals: Int) {
    }

    override fun changeHumanCristalCount(crystals: Int) {
    }

    override fun changePirateCristalCount(crystals: Int) {
    }

    override fun changeRobotCristalCount(crystals: Int) {
    }

    override fun selectCurrentFraction(fractionType: FractionsType) {
    }

    override fun showPlayerBuybackSuccessMessage(name: String) {
    }

    override fun showPlayerBuybackFailureMessage(missingAmount: Int) {
    }
}