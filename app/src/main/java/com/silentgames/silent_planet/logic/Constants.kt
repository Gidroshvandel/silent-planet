package com.silentgames.silent_planet.logic

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
    const val cellImageSize = 31f
    const val entityImageSize = 20f
    const val countCrystalsToWin = 20
}