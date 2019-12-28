package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.BaseProperties
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.mvp.BasePresenter

interface MainContract {
    interface View {

        fun drawBattleGround(gameMatrix: GameMatrix, onUpdateComplete: () -> Unit)

        fun showToast(text: String)

        fun fillEntityName(text: String)

        fun fillDescription(text: String)

        fun showObjectIcon(cellType: CellType)

        fun showObjectIcon(entityType: EntityType)

        fun enableButton(isEnabled: Boolean)

        fun setImageCrystalText(text: String)

        fun showEntityMenuDialog(entityList: MutableList<EntityType>, currentCell: CellType)

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

        fun onEntityDialogElementSelect(baseProperties: BaseProperties)

        fun onCapturedPlayerClick(player: Player)

    }
}
