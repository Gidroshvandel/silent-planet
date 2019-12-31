package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.logic.*
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.BaseProperties
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.doEvent
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.utils.getEntityList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

/**
 * Created by gidroshvandel on 21.06.17.
 */
@ExperimentalCoroutinesApi
@FlowPreview
class MainPresenter internal constructor(
        private val view: MainContract.View,
        private val viewModel: MainViewModel,
        private val model: MainModel
) : MainContract.Presenter, CoroutineScope by MainScope() {

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

    override fun onCapturedPlayerClick(player: Player) {
        viewModel.gameMatrixHelper.gameMatrix.buyBack(
                player,
                {
                    view.showPlayerBuybackSuccessMessage(player.name)
                    checkToWin()
                },
                {
                    view.showPlayerBuybackFailureMessage(it)
                }
        )
    }

    @InternalCoroutinesApi
    override fun onCreate() {
        launch {
            val gameMatrixHelper = GameMatrixHelper(model.generateBattleGround())

            viewModel.gameMatrixHelper = gameMatrixHelper


            TurnHandler.start(Humans)
            Aliens.isPlayable = true
            Humans.isPlayable = true
            Pirates.isPlayable = true
//            Robots.isPlayable = true

            view.changeAlienCristalCount(0)
            view.changeHumanCristalCount(0)
            view.changePirateCristalCount(0)
            view.changeRobotCristalCount(0)

            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {}
            view.selectCurrentFraction(TurnHandler.fractionType)

            view.enableButton(false)

            if (!TurnHandler.getCurrentFraction().isPlayable) {
                tryMoveAi(TurnHandler.fractionType)
            }

            TurnHandler.getFlow().collect {
                view.selectCurrentFraction(it.fractionsType)
                checkToWin()
                if (!it.isPlayable) {
                    tryMoveAi(TurnHandler.fractionType)
                }
            }

        }
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

    private fun tryMove(entity: EntityType, targetPosition: Axis) {
        view.enableButton(false)
        if (viewModel.gameMatrixHelper.gameMatrix.tryMoveEntity(targetPosition, entity)) {
            eventCount = 0
            doEvent(entity) {
                view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
                    TurnHandler.turnCount()
                    updateEntityState(entity)
                }
            }
        } else {
            viewModel.gameMatrixHelper.oldXY = null
            viewModel.gameMatrixHelper.selectedEntity = null
            select(targetPosition)
        }
    }

    private fun tryMoveAi(fractionsType: FractionsType) {
        val player = viewModel.gameMatrixHelper.gameMatrix.choosePlayerToMove(fractionsType)
        view.enableButton(false)
        if (player != null && viewModel.gameMatrixHelper.gameMatrix.moveAi(player)) {
            eventCount = 0
            doEvent(player.entity) {
                view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
                    TurnHandler.turnCount()
                }
            }
        } else {
            TurnHandler.turnCount()
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

    private fun doEvent(entity: EntityType, onUpdateComplete: () -> Unit) {
        view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
            if (viewModel.gameMatrixHelper.gameMatrix.doEvent(entity) && eventCount < 20) {
                eventCount++
                doEvent(entity, onUpdateComplete)
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
