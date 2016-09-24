package com.silentgames.silent_planet.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class BitmapEditor {
    public static Bitmap resize(Bitmap bit, float newWidth, float newHeight) {

        int width = bit.getWidth();
        int height = bit.getHeight();
        float scaleWidth = (newWidth) / width;
        float scaleHeight = (newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bit, 0, 0,
                width, height, matrix, true);
        return resizedBitmap;
    }

    public static Bitmap getEntityBitmap(int DefaultBitId, Resources res){
        Bitmap bitmap = BitmapFactory.decodeResource(res, DefaultBitId);
        return resize(bitmap, Converter.convertDpToPixel(20,res),Converter.convertDpToPixel(20,res));
    }

    public static Bitmap getCellBitmap(int DefaultBitId, Resources res){
        Bitmap bitmap = BitmapFactory.decodeResource(res, DefaultBitId);
        return resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res));
    }


}
