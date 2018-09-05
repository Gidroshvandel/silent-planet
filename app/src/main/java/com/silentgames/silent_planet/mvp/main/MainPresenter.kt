package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.CellProperties
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.utils.getEntityList


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
        select(Axis(x, y))
    }

    override fun onActionButtonClick() {
        viewModel.gameMatrixHelper = getCrystal(viewModel.gameMatrixHelper)
        if (!overZeroCrystals()) {
            view.enableButton(false)
        }
        viewModel.gameMatrixHelper.selectedEntity?.let {
            view.setImageCrystalText(it.crystals.toString())
        }
    }

    override fun onEntityDialogElementSelect(cellProperties: CellProperties) {
        if (cellProperties is EntityType) {
            selectEntity(cellProperties)
        } else if (cellProperties is CellType) {
            selectCell(cellProperties)
        }
    }

    override fun onCreate() {
        val gameMatrixHelper = GameMatrixHelper(model.fillBattleGround())
        gameMatrixHelper.isEventMove = false
        viewModel.gameMatrixHelper = gameMatrixHelper

        view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
        view.enableButton(false)

    }

    private fun select(currentXY: Axis) {
        viewModel.gameMatrixHelper.currentXY = currentXY

        val entityType = viewModel.gameMatrixHelper.gameMatrixCellByXY.entityType
        val cellType = viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType

        if (viewModel.gameMatrixHelper.selectedEntity != null && !isClickForCurrentPosition) {
            tryMove(viewModel.gameMatrixHelper.selectedEntity!!, currentXY)
        } else {
            if (viewModel.gameMatrixHelper.selectedEntity == null
                    && entityType.isNotEmpty()) {
                if (entityType.getEntityList().size > 1) {
                    view.showEntityMenuDialog(entityType, cellType)
                } else {
                    selectEntity(entityType.first())
                }
            } else {
                if (entityType.getEntityList().size > 1) {
                    view.showEntityMenuDialog(entityType, cellType)
                } else {
                    selectCell(cellType)
                }
            }
        }
    }

    private fun selectEntity(entityType: EntityType) {
        updateEntityState(entityType)
        viewModel.gameMatrixHelper.oldXY = viewModel.gameMatrixHelper.currentXY
        viewModel.gameMatrixHelper.selectedEntity = entityType
    }

    private fun updateEntityState(entityType: EntityType) {
        view.setImageCrystalText(entityType.crystals.toString())
        if (overZeroCrystals()) {
            view.enableButton(true)
        } else {
            view.enableButton(false)
        }
        view.showObjectIcon(entityType)
    }

    private fun selectCell(cellType: CellType) {
        view.enableButton(false)
        if (cellType.isVisible) {
            view.setImageCrystalText(cellType.crystals.toString())
        }
        view.showObjectIcon(viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType)
        viewModel.gameMatrixHelper.oldXY = null
        viewModel.gameMatrixHelper.selectedEntity = null
    }

    private fun overZeroCrystals(): Boolean {
        return viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType.crystals > 0
    }

    private fun tryMove(entity: EntityType, currentXY: Axis) {
        view.enableButton(false)
        val newGameMatrix = EntityMove(viewModel.gameMatrixHelper).canMove(entity)
        if (newGameMatrix != null) {
            viewModel.gameMatrixHelper = newGameMatrix
            doEvent()
            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix)
            view.showToast(App.getContext().resources.getString(R.string.turnMessage) + " " + TurnHandler.fractionType.toString())
            updateEntityState(entity)
        } else {
            viewModel.gameMatrixHelper.oldXY = null
            viewModel.gameMatrixHelper.selectedEntity = null
            select(currentXY)
        }
    }

    private fun checkToWin() {

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
        val cellType = gameMatrixHelper.gameMatrixCellByXY.cellType

        if (cellType.crystals > 0) {
            gameMatrixHelper.selectedEntity?.apply { crystals++ }
            cellType.apply { crystals-- }
        }
        return gameMatrixHelper
    }
}
