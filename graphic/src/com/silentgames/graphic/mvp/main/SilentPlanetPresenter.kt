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
                listOf(FractionsType.HUMAN, FractionsType.ALIEN, FractionsType.PIRATE, FractionsType.ROBOT)
        ))
        viewModel.engine.addSystem(AddCrystalSystem())
        viewModel.engine.addSystem(GoalSystem())
        viewModel.engine.addSystem(AiShipSystem())
        viewModel.engine.addSystem(ArrowSystem())
        viewModel.engine.addSystem(TornadoSystem())
        viewModel.engine.addSystem(AbyssSystem())
        viewModel.engine.addSystem(MovementCoordinatesSystem())
        viewModel.engine.addSystem(MoveUnitToShipSystem())
        viewModel.engine.addSystem(SavePathSystem())
        viewModel.engine.addSystem(AntiloopSystem())
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
//                    viewModel.selectedEntity?.let { updateEntityState(it) }
                }
        )

        viewModel.engine.processSystems()

        view.changeAlienCristalCount(0)
        view.changeHumanCristalCount(0)
        view.changePirateCristalCount(0)
        view.changeRobotCristalCount(0)

        view.enableCrystalActionButton(false)
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

            entity.getComponent<Position>()?.currentPosition?.let {
                if (!crystalsOverZero(it)) {
                    view.enableCrystalActionButton(false)
                }
            }
            showEntityInfo(entity)
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
        viewModel.engine.gameState.unitMap.find { it.id == entityData.id }?.addComponent(BuyBackEvent())
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
            else -> this.getComponent<Crystal>()?.count ?: 0
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

    private fun selectEntity(entity: UnitEcs) {
        updateEntityState(entity)
        viewModel.selectedEntity = entity
    }

    private fun updateEntityState(unit: UnitEcs) {
        val position = unit.getComponent<Position>()?.currentPosition
        if (position != null && crystalsOverZero(position)) {
            view.enableCrystalActionButton(true)
        } else {
            view.enableCrystalActionButton(false)
        }
        showEntityInfo(unit)
    }

    private fun crystalsOverZero(position: Axis): Boolean =
            viewModel.engine.gameState.getCell(position)?.getComponent<Crystal>()?.count ?: 0 > 0

    private fun selectCell(cellType: EntityEcs) {
        view.enableCrystalActionButton(false)
        viewModel.selectedEntity = null
        showEntityInfo(cellType)
    }

    private fun tryMove(unit: UnitEcs, targetPosition: Axis) {
        view.enableCrystalActionButton(false)
        unit.addComponent(TargetPosition(targetPosition))
        if (!viewModel.engine.gameState.moveSuccess) {
            viewModel.selectedEntity = null
            select(targetPosition)
        }
    }

    override fun saveInstanceState(onSave: (GameState) -> Unit) {
        viewModel.engine.stop()
        onSave(viewModel.engine.gameState)
    }

    private fun showEntityInfo(data: EntityEcs) {
        showEntityInfo(listOf(data))
    }

    private fun showEntityInfo(dataList: List<EntityEcs>) {
        if (dataList.size == 1 && dataList.first() is UnitEcs) {
            view.changeBottomActionButtonVisibility(true)
        } else {
            view.changeBottomActionButtonVisibility(false)
        }
        view.showEntityMenuDialog(dataList.map())
    }
}
