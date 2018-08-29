package com.silentgames.silent_planet.mvp.main

import android.os.Handler
import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import android.content.ContentValues.TAG




/**
 * Created by gidroshvandel on 21.06.17.
 */
class MainPresenter internal constructor(
        private val view: MainContract.View,
        private val viewModel: MainViewModel,
        private val model: MainModel
) : MainContract.Presenter {

    private val isClickForCurrentPosition: Boolean
        get() = viewModel.gameMatrixHelper.oldXY != null
                && viewModel.gameMatrixHelper.currentXY == viewModel.gameMatrixHelper.oldXY

    override fun onSingleTapConfirmed(x: Int, y: Int) {
        select(Axis(x, y), viewModel.gameMatrixHelper.oldXY, null)
    }

    override fun onCellListItemSelectedClick(x: Int, y: Int, text: String) {
        view.showToast(App.getContext().resources.getString(R.string.selectPlayer) + " " + text)
        selectEntity(Axis(x, y), text)
    }

    override fun onActionButtonClick() {
        viewModel.gameMatrixHelper = getCrystal(viewModel.gameMatrixHelper)
        if (!overZeroCrystals()) {
            view.enableButton(false)
        }
        if (viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType?.spaceShip != null) {
            view.setImageCrystalText(
                    viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType!!.spaceShip!!.crystals.toString())
        } else {
            if (viewModel.gameMatrixHelper.playerName != null)
                view.setImageCrystalText(
                        viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType?.playersOnCell?.getPlayerByName(viewModel.gameMatrixHelper.playerName!!)!!.crystals.toString())
        }
    }

    override fun onCreate() {
        val gameMatrixHelper = GameMatrixHelper(model.fillBattleGround())
        gameMatrixHelper.isEventMove = false
        viewModel.gameMatrixHelper = gameMatrixHelper
        viewModel.isDoubleClick = false


        view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
        view.enableButton(false)

    }

    private fun select(currentXY: Axis, oldXY: Axis?, name: String?) {
        viewModel.gameMatrixHelper.currentXY = currentXY

        val entityType = viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType

        if (oldXY == null || isClickForCurrentPosition) {
            if (entityType != null && !viewModel.isDoubleClick) {
                selectEntity(currentXY, name)
            } else {
                selectCell()
            }
        } else {
            tryMove()
        }
    }

    private fun overZeroCrystals(): Boolean {
        return viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible!!.crystals > 0
    }

    private fun selectEntity(currentXY: Axis, name: String?) {
        val en = viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType
        viewModel.isDoubleClick = true
        if (en?.spaceShip != null) {
            view.setImageCrystalText(en.spaceShip!!.crystals.toString())
        }
        if (name == null) {
            view.showCellListItem(currentXY.x, currentXY.y, model.findPlayerOnCell(viewModel.gameMatrixHelper.gameMatrixCellByXY))
        }
        if (name != null) {
            viewModel.gameMatrixHelper.playerName = name
            if (en?.spaceShip == null) {
                view.setImageCrystalText(en?.playersOnCell?.getPlayerByName(name)!!.crystals.toString())
            }
        }
        if (overZeroCrystals()) {
            view.enableButton(true)
        } else {
            view.enableButton(false)
        }
        viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType?.let { view.showObjectIcon(it) }
        viewModel.gameMatrixHelper.oldXY = currentXY
    }

    private fun selectCell() {
        view.enableButton(false)
        val cellType = viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType
        if (cellType.default == null) {
            view.setImageCrystalText(cellType.onVisible?.crystals.toString())
        }
        view.showObjectIcon(viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType)
        view.hideCellListItem()
        viewModel.gameMatrixHelper.oldXY = null
        viewModel.gameMatrixHelper.playerName = null
        viewModel.isDoubleClick = false
    }

    private fun tryMove() {
        view.enableButton(false)
        val newGameMatrix = EntityMove(viewModel.gameMatrixHelper).canMove()
        if (newGameMatrix != null) {
            viewModel.gameMatrixHelper = newGameMatrix
            doEvent()
            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
            view.showToast(App.getContext().resources.getString(R.string.turnMessage) + " " + TurnHandler.fractionType.toString())
        }
        viewModel.gameMatrixHelper.oldXY = null
        viewModel.gameMatrixHelper.playerName = null
        viewModel.isDoubleClick = false
    }

    private fun checkToWin(){

    }

    private fun doEvent() {
        var count = 0
        while (viewModel.gameMatrixHelper.isEventMove) {
            viewModel.gameMatrixHelper.isEventMove = false
            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
            viewModel.gameMatrixHelper = EntityMove(viewModel.gameMatrixHelper).doEvent()
            count++
            if (count > 20) {
                break
            }
        }
    }

    private fun getCrystal(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        val entityType = gameMatrixHelper.gameMatrixCellByXY.entityType
        val cellType = gameMatrixHelper.gameMatrixCellByXY.cellType

        if (cellType.onVisible!!.crystals > 0) {
            entityType?.playersOnCell?.getPlayerByName(gameMatrixHelper.playerName!!)!!.crystals = entityType.playersOnCell?.getPlayerByName(gameMatrixHelper.playerName!!)!!.crystals + 1
            cellType.onVisible!!.crystals = cellType.onVisible!!.crystals - 1
        }
        return gameMatrixHelper
    }
}
