package com.silentgames.silent_planet.mvp.main

import android.graphics.Bitmap
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.mvp.BasePresenter

interface MainContract {
    interface View {

        fun drawGrid()

        fun drawBattleGround(gameMatrix: Array<Array<Cell>>)

        fun reDraw(eventX: Int, eventY: Int, entity: Bitmap)

        fun showToast(text: String)

        fun showObjectIcon(cellType: CellType)

        fun showObjectIcon(entityType: EntityType)

        fun showCellListItem(x: Int, y: Int, playerList: List<String>)

        fun hideCellListItem()

        fun update(runnable: Runnable)

        fun enableButton(isEnabled: Boolean)

        fun setImageCrystalText(text: String)

    }

    interface Presenter : BasePresenter {

        fun onSingleTapConfirmed(x: Int, y: Int)

        fun onCellListItemSelectedClick(x: Int, y: Int, text: String)

        fun onActionButtonClick()

    }
}
