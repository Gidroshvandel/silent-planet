package com.silentgames.silent_planet.model.entities.ground

import com.silentgames.silent_planet.model.entities.EntityType

import java.util.ArrayList

/**
 * Created by gidroshvandel on 26.09.16.
 */
class PlayersOnCell {
    internal var playerList: MutableList<Player>? = ArrayList()

    fun getPlayerList(): List<Player>? {
        return playerList
    }

    fun setPlayerList(playerList: MutableList<Player>) {
        this.playerList = playerList
    }

    fun add(player: Player) {
        if (playerList == null) {
            playerList = ArrayList()
            playerList!!.add(player)
        } else {
            playerList!!.add(player)
        }
    }

    fun getPlayerByName(name: String): Player? {
        for (player in playerList!!) {
            if (player.playerName === name) {
                return player
            }
        }
        return null
    }

    fun removePlayerFromBoard(player: Player) {
        if (playerList != null) {
            playerList!!.remove(player)
        }
    }

    fun removePlayerByName(name: String) {
        if (playerList!!.size == 1) {
            playerList = null
        } else {
            for (player in playerList!!) {
                if (player.playerName === name) {
                    playerList!!.remove(player)
                    break
                }
            }
        }
    }
}
