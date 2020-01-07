package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.dialog.EntityData
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.logic.ecs.Engine
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.ecs.system.*
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.factionType.Humans
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

    override fun onSingleTapConfirmed(axis: Axis) {
        select(axis)
    }

    override fun onActionButtonClick() {
        val entity = viewModel.selectedEntity
        if (entity != null) {
            entity.addComponent(AddCrystalEvent())
            viewModel.engine.processSystems(entity)

            entity.getComponent<Position>()?.currentPosition?.let {
                if (!crystalsOverZero(it)) {
                    view.enableButton(false)
                }
            }

            entity.getComponent<Crystal>()?.let {
                view.setImageCrystalText(it.count.toString())
            }
        }
    }

    override fun onEntityDialogElementSelect(entityData: EntityData) {
        val entity = viewModel.engine.gameState.unitMap.extractTransports().find { it.id == entityData.id }
        val cell = viewModel.engine.gameState.getCell(entityData.id)
        if (entity != null) {
            selectEntity(entity)
        } else if (cell != null) {
            selectCell(cell)
        }
    }

    override fun onCapturedPlayerClick(entityData: EntityData) {
//        viewModel.gameMatrixHelper.gameMatrix.buyBack(
//                player,
//                {
//                    view.showPlayerBuybackSuccessMessage(player.name)
//                    checkToWin()
//                },
//                {
//                    view.showPlayerBuybackFailureMessage(it)
//                }
//        )
    }

    @InternalCoroutinesApi
    override fun onCreate() {
        launch {
            viewModel.engine = Engine(
                    model.generateNewBattleGround()
            )

            viewModel.engine.addSystem(CaptureSystem())
            viewModel.engine.addSystem(ArrowSystem())
            viewModel.engine.addSystem(TeleportSystem())
            viewModel.engine.addSystem(MovementSystem())
            viewModel.engine.addSystem(ExploreSystem())
            viewModel.engine.addSystem(DeathSystem())
            viewModel.engine.addSystem(CrystalSystem())
            viewModel.engine.addSystem(TransportSystem())
            viewModel.engine.addSystem(
                    model.getRenderSystem {
                        launch {
                            viewModel.selectedEntity?.let { updateEntityState(it) }
                        }
                    }
            )

            viewModel.engine.processSystems()

            TurnHandler.start(Humans)
//            Aliens.isPlayable = true
//            Humans.isPlayable = true
//            Pirates.isPlayable = true
//            Robots.isPlayable = true

            view.changeAlienCristalCount(0)
            view.changeHumanCristalCount(0)
            view.changePirateCristalCount(0)
            view.changeRobotCristalCount(0)

//            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {}
//            view.drawBattleGround(viewModel.engine.gameState) {}
            view.selectCurrentFraction(TurnHandler.fractionType)

            view.enableButton(false)

            if (!TurnHandler.getCurrentFraction().isPlayable) {
//                tryMoveAi(TurnHandler.fractionType)
            }

            TurnHandler.getFlow().collect {
                view.selectCurrentFraction(it.fractionsType)
                checkToWin()
                if (!it.isPlayable) {
//                    tryMoveAi(TurnHandler.fractionType)
                }
            }

        }
    }

    private fun select(currentXY: Axis) {
        val entities = viewModel.engine.gameState.getUnits(currentXY).extractTransports()
        val cellType = viewModel.engine.gameState.getCell(currentXY)

        if (viewModel.selectedEntity != null
                && viewModel.selectedEntity?.getComponent<Position>()?.currentPosition != currentXY
        ) {
            tryMove(viewModel.selectedEntity!!, currentXY)
        } else {
            if (viewModel.selectedEntity == null
                    && entities.isNotEmpty()) {
                if (entities.size > 1) {
                    view.showEntityMenuDialog(entities.map(), cellType?.toEntityData()!!)
                } else {
                    selectEntity(entities.first())
                }
            } else {
                if (entities.size > 1) {
                    view.showEntityMenuDialog(entities.map(), cellType?.toEntityData()!!)
                } else {
                    cellType?.let { selectCell(it) }
                }
            }
        }
    }

    private fun List<Entity>.map() =
            map { it.toEntityData() }.toMutableList()

    private fun Entity.toEntityData(): EntityData {
        val texture = this.getComponent<Texture>()?.bitmap
        val description = this.getComponent<Description>()

        val captured = this.getComponent<Capture>() != null

        val crystal = if (captured) {
            this.getComponent<Capture>()?.buybackPrice ?: 0
        } else {
            this.getComponent<Crystal>()?.count ?: 0
        }

        return EntityData(
                id,
                texture!!,
                description?.name ?: "",
                description?.description ?: "",
                crystal.toString(),
                captured
        )
    }

    private fun showDescription(description: Description) {
        view.fillDescription(description.description)
        view.fillEntityName(description.name)
    }

    private fun selectEntity(entity: Unit) {
        updateEntityState(entity)
        viewModel.selectedEntity = entity
    }

    private fun updateEntityState(unit: Unit) {
        val position = unit.getComponent<Position>()?.currentPosition
        val crystals = unit.getComponent<Crystal>()?.count ?: 0
        view.setImageCrystalText(crystals.toString())
        if (position != null && crystalsOverZero(position)) {
            view.enableButton(true)
        } else {
            view.enableButton(false)
        }
        unit.getComponent<Texture>()?.bitmap?.let { view.showObjectIcon(it) }
        unit.getComponent<Description>()?.let { showDescription(it) }
    }

    private fun crystalsOverZero(position: Axis): Boolean =
            viewModel.engine.gameState.getCell(position)?.getComponent<Crystal>()?.count ?: 0 > 0

    private fun selectCell(cellType: Entity) {
        val crystals = cellType.getComponent<Crystal>()?.count ?: 0
        val isVisible = cellType.getComponent<Hide>() == null
        view.enableButton(false)
        if (isVisible) {
            view.setImageCrystalText(crystals.toString())
        }
        cellType.getComponent<Texture>()?.bitmap?.let { view.showObjectIcon(it) }
        viewModel.selectedEntity = null
        cellType.getComponent<Description>()?.let { showDescription(it) }
    }

    private fun tryMove(unit: Unit, targetPosition: Axis) {
        view.enableButton(false)
        unit.addComponent(TargetPosition(targetPosition))
        viewModel.engine.processSystems(unit)
        if (!viewModel.engine.gameState.moveSuccess) {
            viewModel.selectedEntity = null
            select(targetPosition)
        }
    }

    private fun List<Unit>.extractTransports(): List<Unit> {
        val onBoardEntities = mutableListOf<Unit>()
        forEach {
            val transport = it.getComponent<Transport>()
            if (transport != null) {
                onBoardEntities.addAll(transport.unitsOnBoard)
            }
        }
        return this.toMutableList().apply {
            addAll(onBoardEntities)
        }
    }

//    private fun tryMoveAi(fractionsType: FractionsType) {
//        val player = viewModel.gameMatrixHelper.gameMatrix.choosePlayerToMove(fractionsType)
//        view.enableButton(false)
//        if (player != null && viewModel.gameMatrixHelper.gameMatrix.moveAi(player)) {
//            eventCount = 0
//            doEvent(player.entity) {
//                view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
//                    TurnHandler.turnCount()
//                }
//            }
//        } else if (viewModel.gameMatrixHelper.gameMatrix.moveAiShip(fractionsType)) {
//            view.drawBattleGround(viewModel.gameMatrixHelper.gameMatrix) {
//                TurnHandler.turnCount()
//            }
//        } else {
//            TurnHandler.turnCount()
//        }
//    }

    private fun checkToWin() {
//        val alienCrystals = viewModel.gameMatrixHelper.alienShip.crystals
//        val pirateCrystals = viewModel.gameMatrixHelper.pirateShip.crystals
//        val humanCrystals = viewModel.gameMatrixHelper.humanShip.crystals
//        val robotCrystals = viewModel.gameMatrixHelper.robotShip.crystals
//        view.changeRobotCristalCount(robotCrystals)
//        view.changePirateCristalCount(pirateCrystals)
//        view.changeHumanCristalCount(humanCrystals)
//        view.changeAlienCristalCount(alienCrystals)
//
//        if (alienCrystals >= Constants.countCrystalsToWin) {
//            view.showToast("WIN ALIEN")
//        }
//        if (pirateCrystals >= Constants.countCrystalsToWin) {
//            view.showToast("WIN PIRATE")
//        }
//        if (humanCrystals >= Constants.countCrystalsToWin) {
//            view.showToast("WIN HUMAN")
//        }
//        if (robotCrystals >= Constants.countCrystalsToWin) {
//            view.showToast("WIN ROBOT")
//        }
    }

//    private fun getCrystal(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
//        val cellType = gameMatrixHelper.gameMatrixCellByXY.cellType
//
//        if (cellType.crystals > 0) {
//            gameMatrixHelper.selectedEntity?.apply { crystals++ }
//            cellType.apply { crystals-- }
//        }
//        return gameMatrixHelper
//    }
}
