package com.silentgames.silent_planet.model.cells.defaultCell;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceCellDef extends DefaultCell {
    public SpaceCellDef(Resources res) {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.space_texture,res));
        super.setCanFly(true);
    }
}
