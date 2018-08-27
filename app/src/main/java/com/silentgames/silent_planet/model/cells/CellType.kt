package com.silentgames.silent_planet.model.cells

import com.silentgames.silent_planet.model.CellEx
import com.silentgames.silent_planet.model.cells.defaultCell.Default
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible

/**
 * Created by gidroshvandel on 09.07.16.
 */
class CellType : CellTypeEx {

    var default: Default? = null
    var onVisible: OnVisible? = null

    constructor(onVisible: OnVisible) {
        this.onVisible = onVisible
        all = onVisible
    }

    constructor(aDefault: Default) {
        this.default = aDefault
        all = aDefault
    }

}
