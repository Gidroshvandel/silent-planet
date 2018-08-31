package com.silentgames.silent_planet.model.entities

import com.silentgames.silent_planet.model.CellProperties
import com.silentgames.silent_planet.model.fractions.Fractions

/**
 * Created by gidroshvandel on 27.09.16.
 */
interface EntityTypeProperties : CellProperties {
    var fraction: Fractions
}
