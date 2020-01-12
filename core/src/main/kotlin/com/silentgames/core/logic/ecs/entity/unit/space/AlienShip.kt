package com.silentgames.core.logic.ecs.entity.unit.space


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

class AlienShip(

        position: Axis
) : SpaceShip(
        position,
        Texture("aliens_space_ship"),
        FractionsType.ALIEN,
        Description(
                Strings.alien_ship_name.getString(),
                Strings.alien_ship_description.getString()
        )

)