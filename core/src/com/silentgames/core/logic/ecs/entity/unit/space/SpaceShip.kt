package com.silentgames.core.logic.ecs.entity.unit.space


import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

/**
 * Created by gidroshvandel on 07.07.16.
 */
abstract class SpaceShip(
        position: Axis,
        texture: Texture,
        fractionsType: FractionsType,
        description: Description
) : UnitEcs(
        Position(position),
        texture,
        MovingMode.FLY,
        fractionsType,
        description
) {

    init {
        addComponent(CapitalShip())
        addComponent(LossCrystalImmunityComponent())
        addComponent(TurnMode(GroupType.SHIP))
        addComponent(CrystalBag())
    }

}
