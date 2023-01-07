package com.silentgames.core.logic.ecs.entity.unit.ground

import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

/**
 * Created by gidroshvandel on 24.09.16.
 */
class RobotPlayer(
    name: String,
    position: Axis,
    imageName: String = "robot.png"
) : Player(
    position,
    Texture(imageName),
    FractionsType.ROBOT,
    Description(
        name,
        Strings.robot_player_description.getString()
    )
)
