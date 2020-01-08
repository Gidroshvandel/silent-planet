package com.silentgames.silent_planet.mvp.main

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.dialog.BottomSheetMenu
import com.silentgames.silent_planet.dialog.EntityData
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.view.Callback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), MainContract.View, Callback {

    private lateinit var presenter: MainContract.Presenter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, MainViewModel(), MainModel(this, surface_view))

        initUi()

        presenter.onCreate()

    }

    private fun initUi() {
        action_button.setOnClickListener { presenter.onActionButtonClick() }
    }

    override fun fillEntityName(text: String) {
        tv_entity_name.text = text
    }

    override fun fillDescription(text: String) {
        tv_description.text = text
    }


    override fun selectCurrentFraction(fractionType: FractionsType) {
        val white = ContextCompat.getColor(this, R.color.white)
        val red = ContextCompat.getColor(this, R.color.red)
        tv_humans.setTextColor(white)
        tv_aliens.setTextColor(white)
        tv_robots.setTextColor(white)
        tv_pirates.setTextColor(white)
        when (fractionType) {
            FractionsType.HUMAN -> tv_humans.setTextColor(red)
            FractionsType.ALIEN -> tv_aliens.setTextColor(red)
            FractionsType.ROBOT -> tv_robots.setTextColor(red)
            FractionsType.PIRATE -> tv_pirates.setTextColor(red)
        }
    }

    override fun changeAlienCristalCount(crystals: Int) {
        tv_aliens.text = getString(
                R.string.crystal_count,
                getString(R.string.aliens),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun changeHumanCristalCount(crystals: Int) {
        tv_humans.text = getString(
                R.string.crystal_count,
                getString(R.string.humans),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun changePirateCristalCount(crystals: Int) {
        tv_pirates.text = getString(
                R.string.crystal_count,
                getString(R.string.pirates),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun changeRobotCristalCount(crystals: Int) {
        tv_robots.text = getString(
                R.string.crystal_count,
                getString(R.string.robots),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun enableButton(isEnabled: Boolean) {
        action_button.isEnabled = isEnabled
    }

    override fun setImageCrystalText(text: String) {
        image_crystal_text.text = text
    }

//    private var oldGameMatrix: GameMatrix? = null

//    override fun drawBattleGround(gameMatrix: GameMatrix, onUpdateComplete: () -> Unit) {
//        val sceneLayers = GameMatrixMover(oldGameMatrix, gameMatrix).convertToSceneLayers()
//        oldGameMatrix = gameMatrix.copy()
//        surface_view.updateLayer(SurfaceGameView.LayerType.BACKGROUND, sceneLayers.background)
//        surface_view.updateLayer(SurfaceGameView.LayerType.ENTITY, sceneLayers.entity, onUpdateComplete)
//    }

//    override fun drawBattleGround(gameState: GameState, onUpdateComplete: () -> Unit) {
//        val backgroundLayer = Layer()
//        val entityLayer = Layer()
//        val horizontalCountOfCells = Constants.horizontalCountOfCells
//        val verticalCountOfCells = Constants.verticalCountOfCells
//        for (x in 0 until horizontalCountOfCells) {
//            for (y in 0 until verticalCountOfCells) {
//                backgroundLayer.add(Background(
//                        EngineAxis(x.toFloat(), y.toFloat()),
//                        gameState.getCell(Axis(x, y))?.getComponent<Texture>()?.bitmap!!
//                ))
//                val entity = gameState.getUnit(Axis(x, y))?.getComponent<Texture>()?.bitmap
//                if (entity != null) {
////                    val entity = gameMatrix[x][y].entityType.first()
//                    entityLayer.add(Entity(
//                            "111",
//                            EngineAxis(x.toFloat(), y.toFloat()),
//                            entity
//                    ))
//                }
//            }
//        }
//        surface_view.updateLayer(SurfaceGameView.LayerType.BACKGROUND, backgroundLayer)
//        surface_view.updateLayer(SurfaceGameView.LayerType.ENTITY, entityLayer, onUpdateComplete)
//    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun showObjectIcon(bitmap: Bitmap) {
        select_object_icon.setImageBitmap(bitmap)

    }

    override fun onSingleTapConfirmed(axis: Axis) {
        presenter.onSingleTapConfirmed(axis)
    }

    override fun showEntityMenuDialog(
            entityList: MutableList<EntityData>,
            currentCell: EntityData
    ) {
        BottomSheetMenu(
                this,
                entityList,
                currentCell,
                { entityType ->
                    presenter.onEntityDialogElementSelect(entityType)
                },
                {
                    presenter.onCapturedPlayerClick(it)
                }
        ).show()
    }

    override fun showPlayerBuybackSuccessMessage(name: String) {
        Toast.makeText(this, getString(R.string.player_buyback_success, name), Toast.LENGTH_SHORT).show()
    }

    override fun showPlayerBuybackFailureMessage(missingAmount: Int) {
        Toast.makeText(this, getString(R.string.player_buyback_failure, missingAmount), Toast.LENGTH_SHORT).show()
    }
}
