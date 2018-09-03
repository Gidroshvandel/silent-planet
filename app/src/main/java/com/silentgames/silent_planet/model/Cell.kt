package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType

/**
 * Created by gidroshvandel on 09.07.16.
 */
class Cell(val cellType: CellType, val entityType: MutableList<EntityType> = mutableListOf()) {

//    fun addEntityType(entityType: EntityType) {
//        if (this.entityType == null) {
//            this.entityType = entityType
//        } else {
//            if (entityType.spaceShip != null) {
//                this.entityType!!.spaceShip = entityType.spaceShip
//            }
//            if (entityType.playersOnCell!!.playerList != null) {
//                val playerList = ArrayList<Player>()
//                for (newPlayer in entityType.playersOnCell!!.playerList!!) {
//                    playerList.add(newPlayer)
//                }
//                for (thisPlayer in this.entityType!!.playersOnCell!!.playerList!!) {
//                    playerList.add(thisPlayer)
//                }
//                this.entityType!!.playersOnCell!!.playerList = playerList
//            }
//        }
//    }
}
