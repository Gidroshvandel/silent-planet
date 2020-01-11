package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.logic.ecs.EngineEcs
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs

/**
 * Created by gidroshvandel on 21.06.17.
 */
class MainViewModel {

    lateinit var engine: EngineEcs

    var selectedEntity: UnitEcs? = null

}
