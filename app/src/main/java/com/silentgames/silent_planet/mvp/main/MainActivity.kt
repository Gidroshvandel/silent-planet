package com.silentgames.silent_planet.mvp.main

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.dialog.BottomSheetMenu
import com.silentgames.silent_planet.dialog.EntityData
import com.silentgames.silent_planet.engine.TextureLoader
import com.silentgames.silent_planet.view.Callback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), MainContract.View, Callback {

    private val SAVED_GAME_STATE = "SAVED_GAME_STATE"

    private lateinit var presenter: MainContract.Presenter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(
                this,
                savedInstanceState?.getSerializable(SAVED_GAME_STATE) as? GameState,
                MainViewModel(),
                MainModel(surface_view)
        )

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

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun showObjectIcon(bitmap: String) {
        select_object_icon.setImageBitmap(TextureLoader.load(this, bitmap))
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

    override fun onSaveInstanceState(outState: Bundle?) {
//        presenter.saveInstanceState {
//            outState?.putSerializable(SAVED_GAME_STATE, it)
//        }
        super.onSaveInstanceState(outState)
    }


}
