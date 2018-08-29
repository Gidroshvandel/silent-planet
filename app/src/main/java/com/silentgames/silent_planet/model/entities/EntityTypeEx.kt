package com.silentgames.silent_planet.model.entities

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.CellEx

/**
 * Created by gidroshvandel on 27.09.16.
 */
abstract class EntityTypeEx : CellEx() {
    var fraction: Fractions? = null

    protected fun setAll(defaultClass: EntityTypeEx) {
        super.setAll(defaultClass)
        fraction = defaultClass.fraction
    }
}
