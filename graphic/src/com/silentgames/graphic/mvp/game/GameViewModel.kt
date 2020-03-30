package com.silentgames.graphic.mvp.game

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

/**
 * Created by gidroshvandel on 21.06.17.
 */
class GameViewModel {

    lateinit var engine: EngineEcs

    var selectedEntity: UnitEcs? = null

}
