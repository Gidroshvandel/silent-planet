package com.silentgames.core.logic.ecs.entity.unit.space


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

class PirateShip(
        position: Axis,
        imageName: String = "pirate_space_ship.png"
) : SpaceShip(
        position,
        Texture(imageName),
        FractionsType.PIRATE,
        Description(
                Strings.pirate_ship_name.getString(),
                Strings.pirate_ship_description.getString()
        )
)