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

//    List<Player> playersOnBoard = new ArrayList<>();

    public SpaceShip(Resources res) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.earth_space_ship);
        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(20,res),Converter.convertDpToPixel(20,res)));
        setCanFly(true);
        setCanMove(false);
    }

//    public List<Player> getPlayersOnBoard() {
//        return playersOnBoard;
//    }
//
//    public void setPlayersOnBoard(List<Player> playersOnBoard) {
//        this.playersOnBoard = playersOnBoard;
//    }
//
//    public Player getPlayerByName(String name) {
//        for (Player player: playersOnBoard
//             ) {
//            if(player.getPlayerName() == name){
//                return player;
//            }
//        }
//        return null;
//    }
}
