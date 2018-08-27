package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player

import java.util.ArrayList

/**
 * Created by gidroshvandel on 09.07.16.
 */
class Cell(var cellType: CellType, var entityType: EntityType?) {

    fun addEntityType(entityType: EntityType) {
        if (this.entityType == null) {
            this.entityType = entityType
        } else {
            if (entityType.spaceShip != null) {
                this.entityType!!.spaceShip = entityType.spaceShip
            }
            if (entityType.playersOnCell!!.playerList != null) {
                val playerList = ArrayList<Player>()
                for (newPlayer in entityType.playersOnCell!!.playerList!!) {
                    playerList.add(newPlayer)
                }
                for (thisPlayer in this.entityType!!.playersOnCell!!.playerList!!) {
                    playerList.add(thisPlayer)
                }
                this.entityType!!.playersOnCell!!.playerList = playerList
            }
        }
    }
}
