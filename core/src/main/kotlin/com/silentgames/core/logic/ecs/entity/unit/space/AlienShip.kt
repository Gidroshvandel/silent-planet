package com.silentgames.core.logic.ecs.entity.unit.space


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

class AlienShip(
        position: Axis,
        imageName: String = "aliens_space_ship.png"
) : SpaceShip(
        position,
        Texture(imageName),
        FractionsType.ALIEN,
        Description(
                Strings.alien_ship_name.getString(),
                Strings.alien_ship_description.getString()
        )

)