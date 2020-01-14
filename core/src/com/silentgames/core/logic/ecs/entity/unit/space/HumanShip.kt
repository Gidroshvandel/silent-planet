package com.silentgames.core.logic.ecs.entity.unit.space


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

class HumanShip(
        position: Axis,
        imageName: String = "human_space_ship.png"
) : SpaceShip(
        position,
        Texture(imageName),
        FractionsType.HUMAN,
        Description(
                Strings.human_ship_name.getString(),
                Strings.human_ship_description.getString()
        )
)