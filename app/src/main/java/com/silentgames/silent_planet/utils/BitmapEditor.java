package com.silentgames.silent_planet.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

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
}
