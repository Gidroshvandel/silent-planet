package com.silentgames.graphic.mvp.game

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.mvp.BasePresenter

interface GameContract {
    interface View {

        fun showToast(text: String)

        fun enableCrystalActionButton(isEnabled: Boolean)

        fun enableSkipTurnButton(isEnabled: Boolean)

        fun changeBottomActionButtonVisibility(visible: Boolean)

        fun showEntityMenuDialog(dataList: MutableList<EntityData>)

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

        fun onResume()

        fun onRender()

        fun onTurnSkipped()

        fun onTopScorePanelClick(fractionType: FractionsType)

    }
}
