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

    suspend fun generateBattleGround(
            cellGeneratorParams: CellGeneratorParams = CellGeneratorParams()
    ): Array<Array<Cell>> = withContext(Dispatchers.Default) {
        randomList.clear()
        randomList.addAll(cellGeneratorParams.getRandomEntityList())

        val vCountOfCells = Constants.verticalCountOfCells
        val hCountOfCells = Constants.horizontalCountOfCells

        val randomCellTypeList = MutableList(Constants.countOfGroundCells) {
            randomizeCell()
        }

        randomCellTypeList.shuffle()
        var count = -1

        return@withContext Array(hCountOfCells) { x ->
            Array(vCountOfCells) { y ->
                if (x == 0 || x == hCountOfCells - 1 || y == 0 || y == vCountOfCells - 1) {
                    Cell(SpaceCell(context, Axis(x, y)))
                } else {
                    count++
                    Cell(randomCellTypeList[count].getCellType(Axis(x, y)))
                }
            }
        }
    }


    private fun RandomCellType.getCellType(axis: Axis): CellType {
        return when (this) {
            RandomCellType.DEATH -> DeadCell(context, axis)
            RandomCellType.GREEN_ARROW -> ArrowGreen(context, axis).rotate(BitmapEditor.RotateAngle.randomAngle())
            RandomCellType.RED_ARROW -> ArrowRed(context, axis).rotate(BitmapEditor.RotateAngle.randomAngle())
            RandomCellType.CRYSTAL_ONE -> Crystal(context, axis, CrystalsEnum.ONE)
            RandomCellType.CRYSTAL_TWO -> Crystal(context, axis, CrystalsEnum.TWO)
            RandomCellType.CRYSTAL_THREE -> Crystal(context, axis, CrystalsEnum.THREE)
            RandomCellType.EMPTY -> EmptyCell(context, axis)
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
        val max = maxBy { it.totalCount }
        val index = indexOf(max)
        return Pair(index, max?.totalCount?.minus(max.generatedCount) ?: 0)
    }

}

class CellGeneratorParams(
        private val deathCellCount: Int = 1,
        private val greenArrowCellCount: Int = 20,
        private val redArrowCellCount: Int = 20,
        private val crystalOneCellCount: Int = 10,
        private val crystalTwoCellCount: Int = 5,
        private val crystalThreeCellCount: Int = 5
) {
    private val emptyCount: Int = Constants.countOfGroundCells - (
            deathCellCount +
            greenArrowCellCount +
            redArrowCellCount +
            crystalOneCellCount +
            crystalTwoCellCount +
                    crystalThreeCellCount)

    fun getRandomEntityList() = listOf(
            RandomEntity(RandomCellType.DEATH, deathCellCount),
            RandomEntity(RandomCellType.GREEN_ARROW, greenArrowCellCount),
            RandomEntity(RandomCellType.RED_ARROW, redArrowCellCount),
            RandomEntity(RandomCellType.CRYSTAL_ONE, crystalOneCellCount),
            RandomEntity(RandomCellType.CRYSTAL_TWO, crystalTwoCellCount),
            RandomEntity(RandomCellType.CRYSTAL_THREE, crystalThreeCellCount),
            RandomEntity(RandomCellType.EMPTY, emptyCount)
    )
}

class RandomEntity(val randomCellType: RandomCellType, val totalCount: Int) {
    var generatedCount: Int = 0
        private set

    fun incrementGeneratedCount() {
        generatedCount++
    }

    fun isGenerationComplete() = totalCount <= generatedCount
}

enum class RandomCellType {
    DEATH,
    GREEN_ARROW,
    RED_ARROW,
    CRYSTAL_ONE,
    CRYSTAL_TWO,
    CRYSTAL_THREE,
    EMPTY
}