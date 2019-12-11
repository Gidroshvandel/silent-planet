package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.BaseProperties
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.model.fractions.factionType.Robots
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

    override fun onSingleTapConfirmed(axis: Axis) {
        select(axis)
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

    override fun onEntityDialogElementSelect(baseProperties: BaseProperties) {
        if (baseProperties is EntityType) {
            selectEntity(baseProperties)
        } else if (baseProperties is CellType) {
            selectCell(baseProperties)
        }
    }

    override fun onCreate() {
        val gameMatrixHelper = GameMatrixHelper(model.generateBattleGround())

        TurnHandler.start(Humans)
        TurnHandler.setPlayable(Aliens)
        TurnHandler.setPlayable(Humans)
        TurnHandler.setPlayable(Pirates)
        TurnHandler.setPlayable(Robots)

        gameMatrixHelper.isEventMove = false
        viewModel.gameMatrixHelper = gameMatrixHelper

        view.changeAlienCristalCount(0)
        view.changeHumanCristalCount(0)
        view.changePirateCristalCount(0)
        view.changeRobotCristalCount(0)

        view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {}
        view.selectCurrentFraction(TurnHandler.fractionType)

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

    private fun showDescription(baseProperties: BaseProperties) {
        view.fillDescription(baseProperties.description)
        view.fillEntityName(baseProperties.name)
    }

    private fun selectEntity(entityType: EntityType) {
        updateEntityState(entityType)
        viewModel.gameMatrixHelper.oldXY = viewModel.gameMatrixHelper.currentXY
        viewModel.gameMatrixHelper.selectedEntity = entityType
        showDescription(entityType)
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
        showDescription(cellType)
    }

    private fun overZeroCrystals(): Boolean {
        return viewModel.gameMatrixHelper.gameMatrixCellByXY.cellType.crystals > 0
    }

    private fun tryMove(entity: EntityType, currentXY: Axis) {
        view.enableButton(false)
        val newGameMatrix = EntityMove(viewModel.gameMatrixHelper).canMove(entity)
        if (newGameMatrix != null) {
            viewModel.gameMatrixHelper = newGameMatrix
            eventCount = 0
            doEvent {
                view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
                    TurnHandler.turnCount()
                    view.selectCurrentFraction(TurnHandler.fractionType)
                    updateEntityState(entity)
                    checkToWin()
                }
            }
        } else {
            viewModel.gameMatrixHelper.oldXY = null
            viewModel.gameMatrixHelper.selectedEntity = null
            select(currentXY)
        }
    }

    private fun checkToWin() {
        val alienCrystals = viewModel.gameMatrixHelper.alienShip.crystals
        val pirateCrystals = viewModel.gameMatrixHelper.pirateShip.crystals
        val humanCrystals = viewModel.gameMatrixHelper.humanShip.crystals
        val robotCrystals = viewModel.gameMatrixHelper.robotShip.crystals
        view.changeRobotCristalCount(robotCrystals)
        view.changePirateCristalCount(pirateCrystals)
        view.changeHumanCristalCount(humanCrystals)
        view.changeAlienCristalCount(alienCrystals)

        if (alienCrystals >= Constants.countCrystalsToWin) {
            view.showToast("WIN ALIEN")
        }
        if (pirateCrystals >= Constants.countCrystalsToWin) {
            view.showToast("WIN PIRATE")
        }
        if (humanCrystals >= Constants.countCrystalsToWin) {
            view.showToast("WIN HUMAN")
        }
        if (robotCrystals >= Constants.countCrystalsToWin) {
            view.showToast("WIN ROBOT")
        }
    }

    private var eventCount = 0

    private fun doEvent(onUpdateComplete: () -> Unit) {
        view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
            viewModel.gameMatrixHelper = EntityMove(viewModel.gameMatrixHelper).doEvent()
            if (viewModel.gameMatrixHelper.isEventMove && eventCount < 20) {
                viewModel.gameMatrixHelper.isEventMove = false
                eventCount++
                doEvent(onUpdateComplete)
            } else {
                onUpdateComplete.invoke()
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
