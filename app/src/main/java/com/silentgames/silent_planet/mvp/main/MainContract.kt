package com.silentgames.silent_planet.mvp.main

import android.graphics.Bitmap
import com.silentgames.silent_planet.dialog.EntityData
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.mvp.BasePresenter

interface MainContract {
    interface View {

        fun drawBattleGround(gameState: GameState, onUpdateComplete: () -> Unit)

        fun showToast(text: String)

        fun fillEntityName(text: String)

        fun fillDescription(text: String)

        fun showObjectIcon(bitmap: Bitmap)

        fun enableButton(isEnabled: Boolean)

        fun setImageCrystalText(text: String)

        fun showEntityMenuDialog(entityList: MutableList<EntityData>, currentCell: EntityData)

        fun changeAlienCristalCount(crystals: Int)

        fun changeHumanCristalCount(crystals: Int)

        fun changePirateCristalCount(crystals: Int)

        fun changeRobotCristalCount(crystals: Int)

        fun selectCurrentFraction(fractionType: FractionsType)

        fun showPlayerBuybackSuccessMessage(name: String)

        fun showPlayerBuybackFailureMessage(missingAmount: Int)

    }

    interface Presenter : BasePresenter {

        fun onSingleTapConfirmed(axis: Axis)

        fun onActionButtonClick()

        fun onEntityDialogElementSelect(entityData: EntityData)

        fun onCapturedPlayerClick(entityData: EntityData)

    }
}
