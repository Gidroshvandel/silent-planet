package com.silentgames.silent_planet.mvp.main

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.dialog.EntityData
import com.silentgames.silent_planet.mvp.BasePresenter

interface MainContract {
    interface View {

        fun showToast(text: String)

        fun fillEntityName(text: String)

        fun fillDescription(text: String)

        fun showObjectIcon(bitmap: String)

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

        fun saveInstanceState(onSave: (GameState) -> Unit)
    }
}
