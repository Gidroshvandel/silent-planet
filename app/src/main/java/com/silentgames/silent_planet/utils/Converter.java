package com.silentgames.silent_planet.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class Converter {
    private static int viewSize;
    private static float canvasSize;

    //переводим dp в пиксели
    public static float convertDpToPixel(float dp, Resources res){
        DisplayMetrics metrics = res.getDisplayMetrics();
        return dp * (metrics.densityDpi/160f);
    }
}
