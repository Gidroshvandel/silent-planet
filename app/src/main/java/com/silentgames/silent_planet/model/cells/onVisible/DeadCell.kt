package com.silentgames.silent_planet.model.cells.onVisible

import android.content.res.Resources

import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer
import com.silentgames.silent_planet.utils.BitmapEditor

class DeadCell : OnVisible() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.dead_cell)
        super.isDead = true
        super.isCanMove = true
    }


    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {

        val gameMatrixCell = gameMatrixHelper.gameMatrixCellByXY

        if (gameMatrixCell.cellType.isDead && !gameMatrixCell.entityType!!.isDead) {
            val playerList = PlayersOnCell()
            playerList.add(DeadPlayer(gameMatrixCell.entityType!!.playersOnCell!!.playerList!![0]))
            gameMatrixCell.entityType = EntityType(playerList)
        }

        gameMatrixHelper.gameMatrixCellByXY = gameMatrixCell

        return gameMatrixHelper
    }

}
