package com.silentgames.silent_planet.dialog

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.View
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.BaseProperties
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import kotlinx.android.synthetic.main.dialog_entity_menu.*

class BottomSheetMenu(
        context: Context,
        entityList: MutableList<EntityType>,
        currentCell: CellType,
        onClick: (BaseProperties) -> Unit,
        onCapturedPlayerClick: (Player) -> Unit
) : BottomSheetDialog(context) {

    init {
        val contentView = View.inflate(
                getContext(),
                R.layout.dialog_entity_menu,
                null
        )
        setContentView(contentView)
        rv_menu.adapter = EntityMenuAdapter(
                entityList,
                currentCell,
                {
                    onClick.invoke(it)
                    dismiss()
                },
                {
                    onCapturedPlayerClick.invoke(it)
                    dismiss()
                }
        )
    }

}