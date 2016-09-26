package com.silentgames.silent_planet.model.entities.ground.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.entities.ground.Player;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class DeadPlayer extends Player {
    public DeadPlayer(Player player) {
        super.setBitmap(player.getBitmap());
        super.setPlayerName(player.getPlayerName() + " " + "Мёртв");
        super.setDead(true);
        super.setCanMove(false);
    }
}
