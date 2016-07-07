package com.silentgames.silent_planet.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;
import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceCell {

    private static Bitmap bitmap ;

    private static String properties;

    public SpaceCell() {

    }

    public static Bitmap getBitmap(Resources res, Context context) {
        bitmap = BitmapFactory.decodeResource(res, R.drawable.space_texture);
        bitmap = BitmapEditor.resize(bitmap, Converter.convertDpToPixel(34,context),Converter.convertDpToPixel(34,context));
        return bitmap;
    }

    public static String getProperties() {
        return properties;
    }

    public static void setProperties(String properties) {
        SpaceCell.properties = properties;
    }
}
