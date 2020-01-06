package com.silentgames.silent_planet.logic.ecs.system

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Death
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.utils.notNull

class DeathSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
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

    private fun death(death: Death, unit: Unit) {
        if (death.unit == null) {
            death.unit = unit
            unit.removeComponent(MovingMode.WALK)
            unit.removeComponent(MovingMode.FLY)
            unit.getComponent<Description>()?.let {
                unit.addComponent(it.makeDeathDescription(unit.context))
            }
        }
    }

    private fun Description.makeDeathDescription(context: Context): Description {
        val name = "${this.name} ${context.getString(R.string.dead_player)}"
        val description = context.getString(R.string.dead_player_description)
        return Description(name, description)
    }

}