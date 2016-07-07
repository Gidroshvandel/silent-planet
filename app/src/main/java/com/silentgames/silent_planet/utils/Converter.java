package com.silentgames.silent_planet.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class Converter {
    //переводим dp в пиксели
    public static float convertDpToPixel(float dp,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi/160f);
    }
}
