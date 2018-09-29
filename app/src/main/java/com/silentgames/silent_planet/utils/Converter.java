package com.silentgames.silent_planet.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class Converter {
    //переводим dp в пиксели
    public static float convertDpToPixel(float dp, Resources res){
        DisplayMetrics metrics = res.getDisplayMetrics();
        return dp * (metrics.densityDpi/160f);
    }
}
