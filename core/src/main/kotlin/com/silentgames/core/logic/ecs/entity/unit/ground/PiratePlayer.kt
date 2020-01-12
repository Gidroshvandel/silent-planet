package com.silentgames.core.logic.ecs.entity.unit.ground


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

/**
 * Created by gidroshvandel on 24.09.16.
 */
class PiratePlayer(
        name: String,
        position: Axis
) : Player(
        position,
        Texture("pirate"),
        FractionsType.PIRATE,
        Description(
                name,
                Strings.pirate_player_description.getString())
)
