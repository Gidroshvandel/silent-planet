package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.utils.getAllPlayersFromCell

class DeadCell(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.dead_cell),
        override var isCanMove: Boolean = true
) : CellType(isDead = true) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {

        val gameMatrixCell = gameMatrixHelper.gameMatrixCellByXY

        if (gameMatrixCell.cellType.isDead) {
            val alivePlayers = gameMatrixCell.entityType.getAllPlayersFromCell().filter {
                !it.isDead
            }
            if (alivePlayers.isNotEmpty()) {
                gameMatrixCell.entityType.addAll(killPlayers(alivePlayers.toMutableList()))
            }
        }

        gameMatrixHelper.gameMatrixCellByXY = gameMatrixCell

        return gameMatrixHelper
    }

    private fun killPlayers(alivePlayers: MutableList<Player>): MutableList<DeadPlayer> {
        val deadPlayers: MutableList<DeadPlayer> = mutableListOf()
        alivePlayers.forEach { deadPlayers.add(DeadPlayer(it)) }
        return deadPlayers
    }

}
