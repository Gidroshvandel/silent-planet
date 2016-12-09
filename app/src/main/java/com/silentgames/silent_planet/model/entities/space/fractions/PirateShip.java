package com.silentgames.silent_planet.model.entities.space.fractions;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.model.fractions.factionType.Pirates;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class PirateShip extends SpaceShip {
    public PirateShip(Resources res) {
        super.setBitmap(BitmapEditor.getEntityBitmap(R.drawable.pirate_space_ship,res));
        super.setCanFly(true);
        super.setFraction(Pirates.getInstance());
    }
}
