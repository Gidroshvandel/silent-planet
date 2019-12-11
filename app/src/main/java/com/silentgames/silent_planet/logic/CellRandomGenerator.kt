package com.silentgames.silent_planet.logic

import android.content.Context
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.Arrow.ArrowGreen
import com.silentgames.silent_planet.model.cells.Arrow.ArrowRed
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.cells.Crystal.Crystal
import com.silentgames.silent_planet.model.cells.Crystal.CrystalsEnum
import com.silentgames.silent_planet.model.cells.DeadCell
import com.silentgames.silent_planet.model.cells.EmptyCell
import com.silentgames.silent_planet.model.cells.SpaceCell
import com.silentgames.silent_planet.utils.BitmapEditor
import java.util.*

class CellRandomGenerator(val context: Context) {

    private val randomList = mutableListOf<RandomEntity>()

    fun generateBattleGround(): Array<Array<Cell>> {
        randomList.clear()
        randomList.addAll(RandomCellType.values().map { RandomEntity(it) })

        val vCountOfCells = Constants.verticalCountOfCells
        val hCountOfCells = Constants.horizontalCountOfCells
        return Array(hCountOfCells) { x ->
            Array(vCountOfCells) { y ->
                if (x == 0 || x == hCountOfCells - 1 || y == 0 || y == vCountOfCells - 1) {
                    Cell(SpaceCell(context))
                } else {
                    Cell(getRandomCell(Axis(x, y)))
                }
            }
        }
    }


    private fun getRandomCell(axis: Axis): CellType {
        return when (randomizeCell()) {
            RandomCellType.DEATH -> DeadCell(context)
            RandomCellType.GREEN_ARROW -> ArrowGreen(context).rotate(axis.x, axis.y, BitmapEditor.RotateAngle.randomAngle())
            RandomCellType.RED_ARROW -> ArrowRed(context).rotate(axis.x, axis.y, BitmapEditor.RotateAngle.randomAngle())
            RandomCellType.CRYSTAL_ONE -> Crystal(context, CrystalsEnum.ONE)
            RandomCellType.CRYSTAL_TWO -> Crystal(context, CrystalsEnum.TWO)
            RandomCellType.CRYSTAL_THREE -> Crystal(context, CrystalsEnum.THREE)
            RandomCellType.EMPTY -> EmptyCell(context)
        }
    }

    private fun randomizeCell(): RandomCellType {
        return if (randomList.size > 0) {
            val index = Random().nextInt(randomList.size)
            val cellType = randomList[index]
            if (cellType.isGenerationComplete()) {
                randomList.remove(cellType)
                randomizeCell()
            } else {
                cellType.incrementGeneratedCount()
                cellType.randomCellType
            }
        } else {
            RandomCellType.EMPTY
        }
    }

    private class RandomEntity(val randomCellType: RandomCellType) {
        var generatedCount: Int = 0
            private set

        fun incrementGeneratedCount() {
            generatedCount++
        }

        fun isGenerationComplete() = randomCellType.count <= generatedCount
    }

    private enum class RandomCellType(val count: Int) {
        DEATH(1),
        GREEN_ARROW(10),
        RED_ARROW(10),
        CRYSTAL_ONE(10),
        CRYSTAL_TWO(5),
        CRYSTAL_THREE(5),
        EMPTY(0)
    }

}