package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer
import com.silentgames.silent_planet.utils.BitmapEditor

class DeadCell(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.dead_cell),
        override var isCanMove: Boolean = true
) : CellType(isDead = true) {

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
