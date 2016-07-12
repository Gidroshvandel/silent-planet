package com.silentgames.silent_planet.model.cells;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 13.07.16.
 */
public class EmptyCell extends DefaultClass {
    public EmptyCell(Resources res) {
        setCanFly(true);
        setCanMove(false);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.empty_cell);
        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res)));
    }
}
