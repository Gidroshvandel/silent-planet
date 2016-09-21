package com.silentgames.silent_planet.model.cells.ground;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.model.cells.DefaultCellClass;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class GroundClass extends DefaultCellClass {

    public GroundClass(Resources res) {
//        setCanMove(true);
//        setCanFly(false);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.planet_background);
        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res)));
    }


    public GroundClass() {
    }
}
