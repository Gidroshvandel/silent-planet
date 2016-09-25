package com.silentgames.silent_planet.model.entities.space.fractions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class AlienShip extends SpaceShip {
    public AlienShip(Resources res) {
        super(BitmapEditor.getEntityBitmap(R.drawable.earth_space_ship,res));
    }
}
