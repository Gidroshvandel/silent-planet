package com.silentgames.silent_planet.model.entities.space;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceShip extends DefaultClass {

    List<Player> playersOnBoard = new ArrayList<>();

    public SpaceShip(Bitmap bitmap) {
        setBitmap(bitmap);
        setCanFly(true);
    }

    public List<Player> getPlayersOnBoard() {
        return playersOnBoard;
    }

    public void setPlayersOnBoard(List<Player> playersOnBoard) {
        this.playersOnBoard = playersOnBoard;
    }

    public void addPlayerOnBoard(Player player) {
        if(playersOnBoard == null){
            playersOnBoard = new ArrayList<>();
            playersOnBoard.add(player);
        }else {
            playersOnBoard.add(player);
        }
    }

    public Player getPlayerByName(String name) {
        for (Player player: playersOnBoard
             ) {
            if(player.getPlayerName() == name){
                return player;
            }
        }
        return null;
    }

    public void removePlayerFromBoard(Player player) {
        if(playersOnBoard != null){
            playersOnBoard.remove(player);
        }
    }

    public void removePlayerByName(String name) {
        if(playersOnBoard.size() == 1){
            playersOnBoard = null;
        }else {
            for (Player player : playersOnBoard
                    ) {
                if (player.getPlayerName() == name) {
                    playersOnBoard.remove(player);
                    break;
                }
            }
        }
    }
}
