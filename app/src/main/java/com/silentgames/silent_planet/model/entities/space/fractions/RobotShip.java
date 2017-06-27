package com.silentgames.silent_planet.model.entities.space.fractions;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.model.fractions.factionType.Robots;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 24.09.16.
 */
public class RobotShip extends SpaceShip {
    public RobotShip() {
        super.setBitmap(BitmapEditor.getEntityBitmap(R.drawable.robot_space_ship));
        super.setCanFly(true);
        super.setFraction(Robots.getInstance());
    }
}
