package com.silentgames.silent_planet.model.entities.ground;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 10.07.16.
 */
public class Player extends DefaultClass {
    public Player(Resources res) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.space_man);
        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(20,res),Converter.convertDpToPixel(20,res)));
        setCanFly(false);
        setCanMove(true);
    }
}
