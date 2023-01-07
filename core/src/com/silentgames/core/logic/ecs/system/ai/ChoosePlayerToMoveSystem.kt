package com.silentgames.core.logic.ecs.system.ai

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.stun.StunEffect
import com.silentgames.core.logic.ecs.entity.event.SkipTurnEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.System
import com.silentgames.core.logic.ecs.system.getName

class ChoosePlayerToMoveSystem : System {

    companion object {
        private const val SYSTEM_TAG = "ChoosePlayerToMoveSystem"
    }

    override fun execute(gameState: GameState) {
        if (gameState.isMovingFinish()) {
            gameState.unitMap.firstOrNull { it.hasComponent<ArtificialIntelligence>() }?.let {
                if (it.getComponent<StunEffect>()?.canMove() == false) {
                    CoreLogger.logDebug(SYSTEM_TAG, "SkipTurnEvent")
                    gameState.addEvent(SkipTurnEvent(it))
                }
                it.removeComponent(CanTurn::class.java)
            }
            gameState.unitMap.forEach {
                it.removeComponent(ArtificialIntelligence::class.java)
            }
            if (!gameState.isTurnEnd() &&
                gameState.isPlayersFromCurrentFractionCanTurn() &&
                gameState.aiFractionList.contains(gameState.turn.currentFraction)
            ) {
                val unitToMove = gameState.choosePlayerToMove(gameState.turn.currentFraction)
                unitToMove?.addComponent(ArtificialIntelligence())
                CoreLogger.logDebug(SYSTEM_TAG, "selected unit to move: ${unitToMove?.getName()}")
            } else if (!gameState.isTurnEnd() &&
                gameState.isShipFromCurrentFractionCanTurn() &&
                gameState.aiFractionList.contains(gameState.turn.currentFraction)
            ) {
                val shipToMove = gameState.getCapitalShip(gameState.turn.currentFraction)
                shipToMove?.addComponent(ArtificialIntelligence())
                CoreLogger.logDebug(SYSTEM_TAG, "selected ship to move: ${shipToMove?.getName()}")
            } else if (gameState.aiFractionList.contains(gameState.turn.currentFraction)) {
                CoreLogger.logDebug(SYSTEM_TAG, "SkipTurnEvent")
                gameState.addEvent(SkipTurnEvent())
            }
        }
    }

    private fun GameState.choosePlayerToMove(fractionsType: FractionsType): UnitEcs? {
        val capitalShip = this.getCapitalShip(fractionsType)
        val list = this.unitMap.filter {
            it.getComponent<FractionsType>() == fractionsType &&
                !it.hasComponent<Transport>() &&
                it.hasComponent<Active>() &&
                it.hasComponent<CanTurn>()
        }
        val playerOnGround = list.firstOrNull()
        if (playerOnGround != null) {
            return playerOnGround
        } else {
            capitalShip?.getComponent<Transport>()?.getFirstPlayerFromTransport(fractionsType)
                ?.let {
                    return it
                }
        }
        return null
    }

    private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): UnitEcs? =
        this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

    private fun GameState.isPlayersFromCurrentFractionCanTurn(): Boolean {
        return this.getAllFractionUnits(this.turn.currentFraction)
            .any { it.getComponent<MovingMode>() == MovingMode.WALK && it.hasComponent<CanTurn>() }
    }

    private fun GameState.isShipFromCurrentFractionCanTurn(): Boolean {
        return this.getAllFractionUnits(this.turn.currentFraction)
            .any { it.getComponent<MovingMode>() == MovingMode.FLY && it.hasComponent<CanTurn>() }
    }
}
