package com.silentgames.core.logic.ecs.entity.unit.ground

import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture
import com.silentgames.core.logic.ecs.component.stun.CanStunnedBy
import com.silentgames.core.logic.ecs.component.stun.StunTypeGroup

/**
 * Created by gidroshvandel on 24.09.16.
 */
class HumanPlayer(
    name: String,
    position: Axis,
    imageName: String = "human_spaceman.png"
) : Player(
    position,
    Texture(imageName),
    FractionsType.HUMAN,
    Description(
        name,
        Strings.human_player_description.getString()
    )
) {
    init {
        addComponent(CanStunnedBy(StunTypeGroup.DISEASE))
        addComponent(CanStunnedBy(StunTypeGroup.METEORITES))
    }
}
