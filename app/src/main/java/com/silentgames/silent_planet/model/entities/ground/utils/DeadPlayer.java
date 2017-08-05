package com.silentgames.silent_planet.model.entities.ground.utils;

import com.silentgames.silent_planet.App;
import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.entities.ground.Player;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class DeadPlayer extends Player {
    public DeadPlayer(Player player) {
        super.setBitmap(player.getBitmap());
        super.setPlayerName(player.getPlayerName() + " " + App.getContext().getResources().getString(R.string.deadPlayer));
        super.setDead(true);
        super.setCanMove(false);
        super.setFraction(player.getFraction());
    }
}
