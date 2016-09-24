package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 13.07.16.
 */
public class EmptyCell extends OnVisible {
    public EmptyCell(Resources res) {
        super(BitmapEditor.getCellBitmap(R.drawable.empty_cell,res));
        super.setCanMove(true);
    }
}
