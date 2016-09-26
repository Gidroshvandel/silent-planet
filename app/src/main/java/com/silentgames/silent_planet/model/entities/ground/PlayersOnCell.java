package com.silentgames.silent_planet.model.entities.ground;

import com.silentgames.silent_planet.model.entities.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gidroshvandel on 26.09.16.
 */
public class PlayersOnCell{
    List<Player> playerList = new ArrayList<>();

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void add(Player player) {
        if(playerList == null){
            playerList = new ArrayList<>();
            playerList.add(player);
        }else {
            playerList.add(player);
        }
    }

    public Player getPlayerByName(String name) {
        for (Player player: playerList
                ) {
            if(player.getPlayerName() == name){
                return player;
            }
        }
        return null;
    }

    public void removePlayerFromBoard(Player player) {
        if(playerList != null){
            playerList.remove(player);
        }
    }

    public void removePlayerByName(String name) {
        if(playerList.size() == 1){
            playerList = null;
        }else {
            for (Player player : playerList
                    ) {
                if (player.getPlayerName() == name) {
                    playerList.remove(player);
                    break;
                }
            }
        }
    }
}
