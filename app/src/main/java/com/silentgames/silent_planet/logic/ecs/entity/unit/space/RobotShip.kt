package com.silentgames.silent_planet.logic.ecs.entity.unit.space

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.Texture

class RobotShip(
        context: Context,
        position: Axis
) : SpaceShip(
        context,
        position,
        Texture(R.drawable.robot_space_ship),
        FractionsType.ROBOT,
        Description(
                context.getString(R.string.robot_ship_name),
                context.getString(R.string.robot_ship_description)
        )
)