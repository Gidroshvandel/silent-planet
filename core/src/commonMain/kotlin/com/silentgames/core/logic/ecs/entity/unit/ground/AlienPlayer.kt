package com.silentgames.core.logic.ecs.entity.unit.ground


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

/**
 * Created by gidroshvandel on 24.09.16.
 */
class AlienPlayer(
        name: String,
        position: Axis,
        imageName: String = "alien.png"
) : Player(
        position,
        Texture(imageName),
        FractionsType.ALIEN,
        Description(
                name,
                Strings.alien_player_description.getString()
        )
)
