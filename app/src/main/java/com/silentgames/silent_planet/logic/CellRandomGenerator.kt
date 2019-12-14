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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class CellRandomGenerator(val context: Context) {

    private val random = Random()
    private val randomList = mutableListOf<RandomEntity>()

    suspend fun generateBattleGround(): Array<Array<Cell>> = withContext(Dispatchers.Default) {
        randomList.clear()
        randomList.addAll(RandomCellType.values().map { RandomEntity(it) })

        val vCountOfCells = Constants.verticalCountOfCells
        val hCountOfCells = Constants.horizontalCountOfCells

        val randomCellTypeList = mutableListOf<CellType>()

        for (x in 0 until (Constants.horizontalCountOfGroundCells)) {
            for (y in 0 until (Constants.verticalCountOfGroundCells)) {
                randomCellTypeList.add(getRandomCell(Axis(x, y)))
            }
        }

        randomCellTypeList.shuffle()
        var count = -1

        return@withContext Array(hCountOfCells) { x ->
            Array(vCountOfCells) { y ->
                if (x == 0 || x == hCountOfCells - 1 || y == 0 || y == vCountOfCells - 1) {
                    Cell(SpaceCell(context))
                } else {
                    count++
                    Cell(randomCellTypeList[count])
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

    /**
     * value - значение которое должно часто выпадать
     * сhans - шанс его выпадения в процентах от 0 до 100
     * max - ограничитель рандумных чисел
     *
     * @return от нуля, включительно, до max не включительно
     */
    private fun Random.randPlus(value: Int, chance: Int, max: Int): Int {
        if (chance < 0 || chance > 100) {
            throw IllegalArgumentException("Chance must be between 0 and 100")
        }
        val random = nextInt(100)
        return if (random < chance) {
            value
        } else nextInt(max)
        //Даже при нулевом шансе число всё-таки может выпасть ТУТ.
    }

    private fun randomizeCell(): RandomCellType {
        return if (randomList.size > 0) {
            val pair = randomList.getMaxChance()
            val index = random.randPlus(pair.first, pair.second, randomList.size)
            val cellType = randomList[index]
            println("cellType = " + cellType.randomCellType.name)
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

    private fun List<RandomEntity>.getMaxChance(): Pair<Int, Int> {
        val max = maxBy { it.randomCellType.count }
        val index = indexOf(max)
        return Pair(index, max?.randomCellType?.count!! - max.generatedCount)
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
        EMPTY(59)
    }

}