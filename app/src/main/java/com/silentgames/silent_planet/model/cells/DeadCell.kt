package com.silentgames.silent_planet.model.cells

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Event
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer
import com.silentgames.silent_planet.model.getCell
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.utils.getAllPlayersFromCell
import com.silentgames.silent_planet.utils.getDeathPlayersFromCell

class DeadCell(
        context: Context,
        override val position: Axis,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.dead_cell),
        override var isCanMove: Boolean = true
) : CellType(
        context,
        isDead = true,
        name = context.getString(R.string.death_cell_name),
        description = context.getString(R.string.death_cell_description)
) {

    override fun doEvent(event: Event): Boolean {
        val gameMatrixCell = event.gameMatrix.getCell(position)
        if (gameMatrixCell.cellType.isDead
                && gameMatrixCell.entityType.getDeathPlayersFromCell().isEmpty()) {
            val alivePlayer = gameMatrixCell.entityType.getAllPlayersFromCell().firstOrNull()
            if (alivePlayer != null) {
                gameMatrixCell.entityType.remove(alivePlayer)
                gameMatrixCell.entityType.add(DeadPlayer(alivePlayer))
                return true
            }
        }
        return false
    }

}
