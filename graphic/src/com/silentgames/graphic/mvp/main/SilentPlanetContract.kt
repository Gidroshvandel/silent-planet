package com.silentgames.graphic.mvp.main

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.mvp.BasePresenter

interface SilentPlanetContract {
    interface View {

        fun showToast(text: String)

        fun showEntityInfo(entity: EntityData)

        fun enableButton(isEnabled: Boolean)

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

        fun onActionButtonClick()

        fun onEntityDialogElementSelect(entityData: EntityData)

        fun onCapturedPlayerClick(entityData: EntityData)

        fun saveInstanceState(onSave: (GameState) -> Unit)

        fun onRender()

    }
}
