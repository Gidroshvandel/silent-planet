package com.silentgames.silent_planet.mvp.main

import android.content.Context
import com.silentgames.silent_planet.logic.CellRandomGenerator
import com.silentgames.silent_planet.logic.EntityRandomGenerator
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import java.util.*

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel(val context: Context) {

    suspend fun generateBattleGround(): Array<Array<Cell>> {
        val gameMatrix = CellRandomGenerator(context).generateBattleGround()
        EntityRandomGenerator(context).spawnShips(gameMatrix)
        return gameMatrix
    }

    fun getPlayersNameOnCell(gameMatrixCell: Cell): List<String> {
        val data = ArrayList<String>()

        if (gameMatrixCell.entityType.isNotEmpty()) {
            for (entityType in gameMatrixCell.entityType) {
                if (entityType is SpaceShip) {
                    entityType.playersOnBord.forEach { data.add(it.name) }
                } else if (entityType is Player) {
                    data.add(entityType.name)
                }
            }
        }
        return data
    }

}
