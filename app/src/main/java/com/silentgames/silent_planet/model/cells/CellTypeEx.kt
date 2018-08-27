package com.silentgames.silent_planet.model.cells

import com.silentgames.silent_planet.model.CellEx

/**
 * Created by gidroshvandel on 27.09.16.
 */
abstract class CellTypeEx : CellEx() {

    protected var all: CellTypeEx
        get() = this
        set(cellEx) = super.setAll(cellEx)

}
