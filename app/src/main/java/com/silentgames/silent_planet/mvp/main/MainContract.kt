package com.silentgames.silent_planet.mvp.main

import android.graphics.Bitmap
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.BaseProperties
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.mvp.BasePresenter

interface MainContract {
    interface View {

        fun drawGrid()

        fun drawBattleGround(gameMatrix: Array<Array<Cell>>)

        fun reDraw(eventX: Int, eventY: Int, entity: Bitmap)

        fun showToast(text: String)

        fun fillEntityName(text: String)

        fun fillDescription(text: String)

        fun showObjectIcon(cellType: CellType)

        fun showObjectIcon(entityType: EntityType)

        fun update(runnable: Runnable)

        fun enableButton(isEnabled: Boolean)

        fun setImageCrystalText(text: String)

        fun showEntityMenuDialog(entityList: MutableList<EntityType>, currentCell: CellType)

        fun changeAlienCristalCount(crystals: Int)

        fun changeHumanCristalCount(crystals: Int)

        fun changePirateCristalCount(crystals: Int)

        fun changeRobotCristalCount(crystals: Int)

        fun selectCurrentFraction(fractionType: FractionsType)

    }

    interface Presenter : BasePresenter {

        fun onSingleTapConfirmed(axis: Axis)

        fun onActionButtonClick()

        fun onEntityDialogElementSelect(baseProperties: BaseProperties)

    }
}
