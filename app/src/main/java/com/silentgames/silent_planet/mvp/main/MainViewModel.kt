package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.logic.ecs.Engine
import com.silentgames.silent_planet.logic.ecs.entity.Entity

/**
 * Created by gidroshvandel on 21.06.17.
 */
class MainViewModel {

    lateinit var engine: Engine

    var selectedEntity: Entity? = null

}
