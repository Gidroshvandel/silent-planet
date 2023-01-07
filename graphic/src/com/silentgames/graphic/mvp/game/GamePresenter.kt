package com.silentgames.graphic.mvp.game

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.EntityEcs
import com.silentgames.core.logic.ecs.entity.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.entity.event.BuyBackEvent
import com.silentgames.core.logic.ecs.entity.event.MovementEvent
import com.silentgames.core.logic.ecs.entity.event.SkipTurnEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.ai.*
import com.silentgames.core.logic.ecs.system.cell.ArrowSystem
import com.silentgames.core.logic.ecs.system.cell.TornadoSystem
import com.silentgames.core.logic.ecs.system.event.*
import com.silentgames.core.logic.ecs.system.getCurrentPosition
import com.silentgames.core.logic.ecs.system.unit.*

/**
 * Created by gidroshvandel on 21.06.17.
 */
class GamePresenter internal constructor(
    private val view: GameContract.View,
    private val gameState: GameState?,
    private val viewModel: GameViewModel,
    private val model: GameModel
) : GameContract.Presenter {

    override fun onCreate() {
        viewModel.engine = EngineEcs(
            gameState ?: model.generateNewBattleGround(FractionsType.HUMAN)
        )

        startGame()

        view.changeAlienCristalCount(0)
        view.changeHumanCristalCount(0)
        view.changePirateCristalCount(0)
        view.changeRobotCristalCount(0)

        view.changeBottomActionButtonVisibility(false)
    }

    private fun startGame() {
        viewModel.engine.addSystem(
            BuyBackSystem(
                {
                    view.showPlayerBuybackSuccessMessage(it)
                },
                {
                    view.showPlayerBuybackFailureMessage(it)
                }
            ),
            FindCrystalSystem(),
            PutCrystalSystem(),
            FindShipSystem(),
            AddCrystalSystem(),
            SkipTurnSystem(),
            GoalSystem(),
            AiShipSystem(),
            ArrowSystem(),
            TornadoSystem(),
            AbyssSystem(),
            LossCrystalSystem(),
            MovementCoordinatesSystem(),
            SaveUnitFromSpaceSystem(),
            SavePathSystem(),
            AntiLoopSystem(),
//                CaptureSystem(),
            TeleportSystem(),
            MovementSystem(),
            ExploreSystem(),
            StunSystem(),
            DeathSystem(),
            PutCrystalToCapitalShipSystem(),
            TransportSystem(),
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
            ),
            model.getRenderSystem {
                select(it)
            },
            TurnSystem {
                view.selectCurrentFraction(it)
            },
            ChoosePlayerToMoveSystem(),
            MovingSystem()
        )
    }

    override fun onRender() {
        viewModel.engine.processing()
    }

    private fun select(currentXY: Axis) {
        val entities = viewModel.engine.gameState.getUnits(currentXY)
        val cellType = viewModel.engine.gameState.getCell(currentXY)
        val selectedEntity = viewModel.selectedEntity

        if (selectedEntity != null &&
            selectedEntity.getComponent<Position>()?.currentPosition != currentXY
        ) {
            tryMove(selectedEntity, currentXY)
        } else {
            if (entities.size > 1) {
                val listToShow: MutableList<EntityEcs> = entities.toMutableList()
                cellType?.let { listToShow.add(it) }
                showEntityInfo(listToShow)
            } else {
                if (selectedEntity == null &&
                    entities.isNotEmpty()
                ) {
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
            viewModel.engine.addEvent(AddCrystalEvent(entityEcs = entity))
            showEntityInfo(entity)
        }
    }

    override fun onTurnSkipped() {
        viewModel.selectedEntity?.let {
            viewModel.engine.addEvent(SkipTurnEvent(it))
        }
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
            viewModel.engine.addEvent(BuyBackEvent(it))
        }
    }

    override fun onTopScorePanelClick(fractionType: FractionsType) {
        if (fractionType == viewModel.engine.gameState.turn.currentFraction) {
            viewModel.engine.addEvent(SkipTurnEvent())
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
        return (
            viewModel.engine.gameState.getCell(position)
                ?.getComponent<Crystal>()?.count ?: 0 > 0 &&
                this.getComponent<CrystalBag>()?.canGetCrystal() == true
            )
    }

    private fun tryMove(unit: UnitEcs, targetPosition: Axis) {
        viewModel.engine.addEvent(MovementEvent(targetPosition, unit))
        if (unit.hasComponent<Moving>()) {
            updateSelectedEntity()
        } else {
            viewModel.selectedEntity = null
            select(targetPosition)
        }
    }

    override fun saveInstanceState(onSave: (GameState) -> Unit) {
        viewModel.engine.stop()
        onSave(viewModel.engine.gameState)
    }

    override fun onResume() {
        startGame()
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
        viewModel.engine.gameState.turn.currentFraction == getComponent<FractionsType>()
}
