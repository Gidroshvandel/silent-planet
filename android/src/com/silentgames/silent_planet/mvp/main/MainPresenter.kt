package com.silentgames.silent_planet.mvp.main

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.EntityEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.silent_planet.dialog.EntityData

/**
 * Created by gidroshvandel on 21.06.17.
 */
class MainPresenter internal constructor(
    private val view: MainContract.View,
    private val gameState: GameState?,
    private val viewModel: MainViewModel,
    private val model: MainModel
) : MainContract.Presenter {

    override fun onCreate() {
//        viewModel.engine = EngineEcs(
//                gameState ?: model.generateNewBattleGround(FractionsType.HUMAN)
//        )
//
//        viewModel.engine.addSystem(BuyBackSystem(
//                {
//                    view.showPlayerBuybackSuccessMessage(it)
//                },
//                {
//                    view.showPlayerBuybackFailureMessage(it)
//                }
//        ))
//
//        viewModel.engine.addSystem(ChoosePlayerToMoveSystem(
//                listOf(FractionsType.HUMAN, FractionsType.ALIEN, FractionsType.PIRATE, FractionsType.ROBOT))
//        )
//        viewModel.engine.addSystem(AddCrystalSystem())
//        viewModel.engine.addSystem(GoalSystem())
//        viewModel.engine.addSystem(AiShipSystem())
//        viewModel.engine.addSystem(ArrowSystem())
//        viewModel.engine.addSystem(TornadoSystem())
//        viewModel.engine.addSystem(MovementSystem())
//        viewModel.engine.addSystem(TeleportSystem())
// //        viewModel.engine.addSystem(CaptureSystem())
// //        viewModel.engine.addSystem(TeleportSystem())
//        viewModel.engine.addSystem(ExploreSystem())
//        viewModel.engine.addSystem(DeathSystem())
//        viewModel.engine.addSystem(PutCrystalToCapitalShipSystem())
//        viewModel.engine.addSystem(TransportSystem())
//        viewModel.engine.addSystem(
//                WinSystem(
//                        Constants.countCrystalsToWin,
//                        { fractionsType, crystals ->
//                            when (fractionsType) {
//                                FractionsType.ALIEN -> view.changeAlienCristalCount(crystals)
//                                FractionsType.HUMAN -> view.changeHumanCristalCount(crystals)
//                                FractionsType.PIRATE -> view.changePirateCristalCount(crystals)
//                                FractionsType.ROBOT -> view.changeRobotCristalCount(crystals)
//                            }
//                        },
//                        {
//                            view.showToast("WIN " + it.name)
//                        }
//                )
//        )
//
//        viewModel.engine.addSystem(
//                TurnSystem {
//                    view.selectCurrentFraction(it)
//                }
//        )
// //        viewModel.engine.addSystem(
// //                model.getRenderSystem {
// //                    viewModel.selectedEntity?.let { updateEntityState(it) }
// //                }
// //        )
//
//        viewModel.engine.processSystems()
//
//        view.changeAlienCristalCount(0)
//        view.changeHumanCristalCount(0)
//        view.changePirateCristalCount(0)
//        view.changeRobotCristalCount(0)
//
//        view.enableButton(false)
    }

    override fun onSingleTapConfirmed(axis: Axis) {
        select(axis)
    }

    private fun select(currentXY: Axis) {
        val entities = viewModel.engine.gameState.getUnits(currentXY)
        val cellType = viewModel.engine.gameState.getCell(currentXY)

        if (viewModel.selectedEntity != null &&
            viewModel.selectedEntity?.getComponent<Position>()?.currentPosition != currentXY
        ) {
            tryMove(viewModel.selectedEntity!!, currentXY)
        } else {
            if (viewModel.selectedEntity == null &&
                entities.isNotEmpty()
            ) {
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

    override fun onActionButtonClick() {
        val entity = viewModel.selectedEntity
        if (entity != null) {
//            entity.addComponent(AddCrystalEventComponent())

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
        val entity = viewModel.engine.gameState.unitMap.find { it.id == entityData.id }
        val cell = viewModel.engine.gameState.getCell(entityData.id)
        if (entity != null) {
            selectEntity(entity)
        } else if (cell != null) {
            selectCell(cell)
        }
    }

    override fun onCapturedPlayerClick(entityData: EntityData) {
//        viewModel.engine.gameState.unitMap.find { it.id == entityData.id }?.addComponent(BuyBackEventComponent())
    }

    private fun List<EntityEcs>.map() =
        map { it.toEntityData() }.toMutableList()

    private fun EntityEcs.toEntityData(): EntityData {
        val texture = this.getComponent<Texture>()?.bitmapName
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

    private fun selectEntity(entity: UnitEcs) {
        updateEntityState(entity)
        viewModel.selectedEntity = entity
    }

    private fun updateEntityState(unit: UnitEcs) {
        val position = unit.getComponent<Position>()?.currentPosition
        val crystals = unit.getComponent<Crystal>()?.count ?: 0
        view.setImageCrystalText(crystals.toString())
        if (position != null && crystalsOverZero(position)) {
            view.enableButton(true)
        } else {
            view.enableButton(false)
        }
        unit.getComponent<Texture>()?.bitmapName?.let { view.showObjectIcon(it) }
        unit.getComponent<Description>()?.let { showDescription(it) }
    }

    private fun crystalsOverZero(position: Axis): Boolean =
        viewModel.engine.gameState.getCell(position)?.getComponent<Crystal>()?.count ?: 0 > 0

    private fun selectCell(cellType: EntityEcs) {
        val crystals = cellType.getComponent<Crystal>()?.count ?: 0
        val isVisible = cellType.getComponent<Hide>() == null
        view.enableButton(false)
        if (isVisible) {
            view.setImageCrystalText(crystals.toString())
        }
        cellType.getComponent<Texture>()?.bitmapName?.let { view.showObjectIcon(it) }
        viewModel.selectedEntity = null
        cellType.getComponent<Description>()?.let { showDescription(it) }
    }

    private fun tryMove(unit: UnitEcs, targetPosition: Axis) {
        view.enableButton(false)
//        unit.addComponent(TargetPosition(targetPosition))
//        if (!viewModel.engine.gameState.moveSuccess) {
//            viewModel.selectedEntity = null
//            select(targetPosition)
//        }
    }

    override fun saveInstanceState(onSave: (GameState) -> Unit) {
        viewModel.engine.stop()
        onSave(viewModel.engine.gameState)
    }
}
