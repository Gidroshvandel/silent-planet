package com.silentgames.silent_planet.model.entities

import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell
import com.silentgames.silent_planet.model.entities.space.SpaceShip

/**
 * Created by gidroshvandel on 09.07.16.
 */

class EntityType : EntityTypeEx {

    var playersOnCell: PlayersOnCell? = null

    var spaceShip: SpaceShip? = null

    constructor(player: PlayersOnCell) {
        this.playersOnCell = player
        setAll(player.playerList!![0])
    }

    constructor(spaceShip: SpaceShip) {
        this.spaceShip = spaceShip
        setAll(spaceShip)
    }
}
