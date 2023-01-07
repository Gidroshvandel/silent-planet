package com.silentgames.core.logic

/**
 * Created by gidroshvandel on 09.07.16.
 */
object Constants {
    const val horizontalCountOfCells = 12
    private const val horizontalCountOfSpaceCells = 2
    const val horizontalCountOfGroundCells = horizontalCountOfCells - horizontalCountOfSpaceCells
    const val verticalCountOfCells = 12
    private const val verticalCountOfSpaceCells = 2
    const val verticalCountOfGroundCells = verticalCountOfCells - verticalCountOfSpaceCells
    const val countOfGroundCells = verticalCountOfGroundCells * horizontalCountOfGroundCells
    const val cellImageSize = 31f
    const val entityImageSize = 20f
    const val countCrystalsToWin = 13
}
