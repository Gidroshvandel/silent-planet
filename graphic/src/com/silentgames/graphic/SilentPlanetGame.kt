package com.silentgames.graphic

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.silentgames.core.logic.CellRandomGenerator
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.EntityRandomGenerator
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.system.*

class SilentPlanetGame : ApplicationAdapter() {

    private val screenRect = Rectangle(0f, 0f, WIDTH * 2, HEIGHT * 2)
    private val viewPort = FitViewport(WIDTH, HEIGHT)
    private val camera by lazy(viewPort::getCamera)
    private val engine by lazy {
        EngineEcs(generateNewBattleGround(FractionsType.HUMAN))
    }

    override fun create() {
        (camera as? OrthographicCamera)?.zoom = 1f
        camera.position.x = WIDTH / 2f
        camera.position.y = HEIGHT / 2f
        engine.addSystem(BuyBackSystem(
                {
                },
                {
                }
        ))

        engine.addSystem(AiPlayerSystem(listOf(FractionsType.HUMAN, FractionsType.ALIEN, FractionsType.PIRATE, FractionsType.ROBOT)))
        engine.addSystem(AddCrystalSystem())
        engine.addSystem(GoalSystem())
        engine.addSystem(AiShipSystem())
        engine.addSystem(ArrowSystem())
        engine.addSystem(MovementSystem())
        engine.addSystem(TeleportSystem())
//        engine.addSystem(CaptureSystem())
//        engine.addSystem(TeleportSystem())
        engine.addSystem(ExploreSystem())
        engine.addSystem(DeathSystem())
        engine.addSystem(PutCrystalToCapitalShipSystem())
        engine.addSystem(TransportSystem())
        engine.addSystem(
                WinSystem(
                        Constants.countCrystalsToWin,
                        { fractionsType, crystals ->
                            when (fractionsType) {
                            }
                        },
                        {
                        }
                )
        )

        engine.addSystem(
                TurnSystem {
                }
        )
        engine.addSystem(RenderSystem(camera, SpriteBatch()))
    }

    var first = true

    override fun render() {
//        if (first) {
        first = false
        Gdx.gl.glClearColor(0.5f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if (engine.processing) {
            engine.systems.find { it is RenderSystem }?.execute(engine.gameState)
        } else
            engine.processSystems()
//        }
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
}