package com.silentgames.core.logic.ecs.entity.unit.ground


import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.stun.CanStunnedBy
import com.silentgames.core.logic.ecs.component.stun.StunTypeGroup
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

/**
 * Created by gidroshvandel on 10.07.16.
 */
abstract class Player(
        position: Axis,
        texture: Texture,
        fractionsType: FractionsType,
        description: Description
) : UnitEcs(
        Position(position),
        texture,
        MovingMode.WALK,
        fractionsType,
        description
) {

    init {
        addComponent(CanStunnedBy(StunTypeGroup.SWELL))
        addComponent(CanStunnedBy(StunTypeGroup.CRATER))
        addComponent(TurnMode(GroupType.PLAYER))
        addComponent(CrystalBag(maxCrystals = 1))
    }

}
