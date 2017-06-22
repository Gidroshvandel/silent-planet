package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 09.12.16.
 */
public abstract class Arrow extends OnVisible {

    public abstract ArrowGreen rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle);

}
