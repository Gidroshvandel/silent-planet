package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.cells.DefaultCellClass;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

public class DeadCell extends DefaultCellClass {

    public DeadCell(Resources res) {
        setDead(true);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.dead_cell);
        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res)));
    }
}
