package com.silentgames.silent_planet.model.entities.space;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceShip extends DefaultClass {

    public SpaceShip(Resources res) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.earth_space_ship);
        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(20,res),Converter.convertDpToPixel(20,res)));
        setCanFly(true);
        setCanMove(false);
    }

}
