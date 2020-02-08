package com.silentgames.graphic.mvp.main

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.component.event.BuyBackEvent
import com.silentgames.core.logic.ecs.entity.EntityEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.*
import com.silentgames.graphic.RenderSystem

/**
 * Created by gidroshvandel on 21.06.17.
 */
class SilentPlanetPresenter internal constructor(
        private val view: SilentPlanetContract.View,
        private val gameState: GameState?,
        private val viewModel: SilentPlanetViewModel,
        private val model: SilentPlanetModel
) : SilentPlanetContract.Presenter {

    override fun onCreate() {
        viewModel.engine = EngineEcs(
                gameState ?: model.generateNewBattleGround(FractionsType.HUMAN)
        )

        viewModel.engine.addSystem(BuyBackSystem(
                {
                    view.showPlayerBuybackSuccessMessage(it)
                },
                {
                    view.showPlayerBuybackFailureMessage(it)
                }
        ))

        viewModel.engine.addSystem(AiPlayerSystem(
//                listOf(FractionsType.HUMAN, FractionsType.ALIEN, FractionsType.PIRATE, FractionsType.ROBOT)
        ))
        viewModel.engine.addSystem(AddCrystalSystem())
        viewModel.engine.addSystem(GoalSystem())
        viewModel.engine.addSystem(AiShipSystem())
        viewModel.engine.addSystem(ArrowSystem())
        viewModel.engine.addSystem(TornadoSystem())
        viewModel.engine.addSystem(AbyssSystem())
        viewModel.engine.addSystem(LossCrystalSystem())
        viewModel.engine.addSystem(MovementCoordinatesSystem())
        viewModel.engine.addSystem(SaveUnitFromSpaceSystem())
        viewModel.engine.addSystem(SavePathSystem())
        viewModel.engine.addSystem(AntiLoopSystem())
//        viewModel.engine.addSystem(CaptureSystem())
        viewModel.engine.addSystem(MovementSystem())
        viewModel.engine.addSystem(TeleportSystem())
        viewModel.engine.addSystem(ExploreSystem())
        viewModel.engine.addSystem(DeathSystem())
        viewModel.engine.addSystem(PutCrystalToCapitalShipSystem())
        viewModel.engine.addSystem(TransportSystem())
        viewModel.engine.addSystem(
                WinSystem(
                        Constants.countCrystalsToWin,
                        { fractionsType, crystals ->
                            when (fractionsType) {
                                FractionsType.ALIEN -> view.changeAlienCristalCount(crystals)
                                FractionsType.HUMAN -> view.changeHumanCristalCount(crystals)
                                FractionsType.PIRATE -> view.changePirateCristalCount(crystals)
                                FractionsType.ROBOT -> view.changeRobotCristalCount(crystals)
                            }
                        },
                        {
                            view.showToast("WIN " + it.name)
                        }
                )
        )

        viewModel.engine.addSystem(
                TurnSystem {
                    view.selectCurrentFraction(it)
                }
        )
        viewModel.engine.addSystem(
                model.getRenderSystem {
                    select(it)
                }
        )

        viewModel.engine.processSystems()

        view.changeAlienCristalCount(0)
        view.changeHumanCristalCount(0)
        view.changePirateCristalCount(0)
        view.changeRobotCristalCount(0)

        view.changeBottomActionButtonVisibility(false)
    }

    override fun onRender() {
        if (viewModel.engine.processing) {
            viewModel.engine.systems.find { it is RenderSystem }?.execute(viewModel.engine.gameState)
        } else
            viewModel.engine.processSystems()
    }

    private fun select(currentXY: Axis) {
        val entities = viewModel.engine.gameState.getUnits(currentXY)
        val cellType = viewModel.engine.gameState.getCell(currentXY)
        val selectedEntity = viewModel.selectedEntity

        if (selectedEntity != null
                && selectedEntity.getComponent<Position>()?.currentPosition != currentXY
        ) {
            tryMove(selectedEntity, currentXY)
        } else {
            if (entities.size > 1) {
                val listToShow: MutableList<EntityEcs> = entities.toMutableList()
                cellType?.let { listToShow.add(it) }
                showEntityInfo(listToShow)
            } else {
                if (selectedEntity == null
                        && entities.isNotEmpty()) {
                    selectEntity(entities.first())
                } else {
                    cellType?.let { selectCell(it) }
                }
            }
        }
    }

    override fun onActionButtonClick() {
        val entity = viewModel.selectedEntity
        if (entity != null) {
            entity.addComponent(AddCrystalEvent())
            viewModel.engine.processSystems()
            showEntityInfo(entity)
        }
    }

    override fun onTurnSkipped() {
        viewModel.selectedEntity?.removeComponent(CanTurn::class.java)
        viewModel.engine.processSystems()
        updateSelectedEntity()
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
        viewModel.engine.gameState.unitMap.find { it.id == entityData.id }?.let {
            it.addComponent(BuyBackEvent())
            viewModel.engine.processSystems()
        }
    }

    override fun onTopScorePanelClick(fractionType: FractionsType) {
        if (fractionType == viewModel.engine.gameState.turn.currentTurnFraction) {
            viewModel.engine.gameState.endTurn()
            viewModel.engine.processSystems()
        }
    }

    private fun List<EntityEcs>.map() =
            map { it.toEntityData() }.toMutableList()

    private fun EntityEcs.toEntityData(): EntityData {
        val texture = this.getComponent<Texture>()?.bitmapName
        val description = this.getComponent<Description>()

        val captured = this.getComponent<Capture>() != null

        val crystal = when {
            this.hasComponent<Hide>() -> 0
            captured -> this.getComponent<Capture>()?.buybackPrice ?: 0
            else -> this.getComponent<Crystal>()?.count ?: this.getComponent<CrystalBag>()?.amount
            ?: 0
        }

        return EntityData(
                id,
                texture ?: "",
                description?.name ?: "",
                description?.description ?: "",
                crystal.toString(),
                captured
        )
    }

    private fun selectEntity(unit: UnitEcs) {
        showEntityInfo(unit)
        viewModel.selectedEntity = unit
    }

    private fun selectCell(cellType: EntityEcs) {
        viewModel.selectedEntity = null
        showEntityInfo(cellType)
    }

    private fun UnitEcs.canGetCrystals(): Boolean {
        val position = this.getCurrentPosition() ?: return false
        return (viewModel.engine.gameState.getCell(position)?.getComponent<Crystal>()?.count ?: 0 > 0
                && this.getComponent<CrystalBag>()?.canGetCrystal() == true)
    }

    private fun tryMove(unit: UnitEcs, targetPosition: Axis) {
        unit.addComponent(TargetPosition(targetPosition))
        viewModel.engine.processSystems()
        if (viewModel.engine.gameState.unitMap.find { it.hasComponent<MovedSuccess>() } == null) {
            viewModel.selectedEntity = null
            select(targetPosition)
        } else {
            updateSelectedEntity()
        }
    }

    override fun saveInstanceState(onSave: (GameState) -> Unit) {
        viewModel.engine.stop()
        onSave(viewModel.engine.gameState)
    }

    private fun updateSelectedEntity() {
        viewModel.selectedEntity?.let {
            showEntityInfo(it)
        }
    }

    private fun showEntityInfo(data: EntityEcs) {
        showEntityInfo(listOf(data))
    }

    private fun showEntityInfo(dataList: List<EntityEcs>) {
        val firstElement = dataList.firstOrNull()
        if (dataList.size == 1 && firstElement is UnitEcs && firstElement.isCurrentTurn()) {
            view.enableCrystalActionButton(firstElement.canGetCrystals())
            view.enableSkipTurnButton(firstElement.hasComponent<CanTurn>())
            view.changeBottomActionButtonVisibility(true)
        } else {
            view.changeBottomActionButtonVisibility(false)
        }
        view.showEntityMenuDialog(dataList.map())
    }

    private fun UnitEcs.isCurrentTurn() =
            viewModel.engine.gameState.turn.currentTurnFraction == getComponent<FractionsType>()
}
