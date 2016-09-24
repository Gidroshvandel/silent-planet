package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;

public class DeadCell extends OnVisible {

    public DeadCell(Resources res) {
        super(BitmapEditor.getCellBitmap(R.drawable.dead_cell,res));
        super.setDead(true);
        super.setCanMove(true);
    }
}
