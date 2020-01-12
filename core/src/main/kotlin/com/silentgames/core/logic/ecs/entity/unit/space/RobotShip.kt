package com.silentgames.core.logic.ecs.entity.unit.space


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Texture

class RobotShip(

        position: Axis
) : SpaceShip(

        position,
        Texture("robot_space_ship"),
        FractionsType.ROBOT,
        Description(
                Strings.robot_ship_name.getString(),
                Strings.robot_ship_description.getString()
        )
)