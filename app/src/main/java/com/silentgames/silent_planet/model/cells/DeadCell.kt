package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.utils.getAllPlayersFromCell
import com.silentgames.silent_planet.utils.getDeathPlayersFromCell

class DeadCell(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.dead_cell),
        override var isCanMove: Boolean = true
) : CellType(
        isDead = true,
        name = App.getContext().getString(R.string.death_cell_name),
        description = App.getContext().getString(R.string.death_cell_description)
) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {

        val gameMatrixCell = gameMatrixHelper.gameMatrixCellByXY

        if (gameMatrixCell.cellType.isDead
                && gameMatrixCell.entityType.getDeathPlayersFromCell().isEmpty()) {
            val alivePlayer = gameMatrixCell.entityType.getAllPlayersFromCell().firstOrNull()
            if (alivePlayer != null) {
                gameMatrixCell.entityType.remove(alivePlayer)
                gameMatrixCell.entityType.add(DeadPlayer(alivePlayer))
            }
        }

        gameMatrixHelper.gameMatrixCellByXY = gameMatrixCell

        return gameMatrixHelper
    }

}
