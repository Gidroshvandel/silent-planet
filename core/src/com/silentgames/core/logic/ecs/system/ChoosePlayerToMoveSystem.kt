package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.event.SkipTurnEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class ChoosePlayerToMoveSystem(private val aiFractionList: List<FractionsType> = listOf()) : System {
    override fun execute(gameState: GameState) {
        if (!gameState.isTurnEnd() && gameState.isPlayersFromCurrentFractionCanTurn() && aiFractionList.contains(gameState.turn.currentTurnFraction)) {
            gameState.choosePlayerToMove(gameState.turn.currentTurnFraction)?.addComponent(ArtificialIntelligence())
        } else if (aiFractionList.contains(gameState.turn.currentTurnFraction)) {
            gameState.addEvent(SkipTurnEvent())
        }
    }

    private fun GameState.choosePlayerToMove(fractionsType: FractionsType): UnitEcs? {
        val capitalShip = this.getCapitalShip(fractionsType)
        val list = this.unitMap.filter {
            it.getComponent<FractionsType>() == fractionsType
                    && !it.hasComponent<Transport>()
                    && it.hasComponent<Active>()
                    && it.hasComponent<CanTurn>()
        }
        val playerOnGround = list.firstOrNull()
        if (playerOnGround != null) {
            return playerOnGround
        } else {
            capitalShip?.getComponent<Transport>()?.getFirstPlayerFromTransport(fractionsType)?.let {
                return it
            }
        }
        return null
    }

    private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): UnitEcs? =
            this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

    private fun GameState.isPlayersFromCurrentFractionCanTurn(): Boolean {
        return this.getAllFractionUnits(this.turn.currentTurnFraction)
                .any { it.getComponent<MovingMode>() == MovingMode.WALK && it.hasComponent<CanTurn>() }
    }
}