package com.silentgames.silent_planet.model.entities.space.fractions;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class RobotShip extends SpaceShip {
    public RobotShip(Resources res) {
        super(BitmapEditor.getEntityBitmap(R.drawable.robot_space_ship, res));
        setCanFly(true);
    }
}
