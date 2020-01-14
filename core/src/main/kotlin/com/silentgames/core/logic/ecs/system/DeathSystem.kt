package com.silentgames.core.logic.ecs.system


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Active
import com.silentgames.core.logic.ecs.component.Death
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class DeathSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        val position = unit.getComponent<Position>()?.currentPosition
        if (position != null) {
            val cell = gameState.getCell(position)
            notNull(
                    cell?.getComponent(),
                    unit,
                    ::death
            )
        }
    }

    private fun death(death: Death, unit: UnitEcs) {
        if (death.unit == null) {
            death.unit = unit
            unit.removeComponent(Active::class.java)
            unit.getComponent<Description>()?.let {
                unit.addComponent(it.makeDeathDescription())
            }
        }
    }

    private fun Description.makeDeathDescription(): Description {
        val name = "${this.name} ${Strings.dead_player.getString()}"
        val description = Strings.dead_player_description.getString()
        return Description(name, description)
    }

}