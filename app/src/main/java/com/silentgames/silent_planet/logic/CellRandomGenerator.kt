package com.silentgames.silent_planet.logic

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.RotateAngle
import com.silentgames.silent_planet.logic.ecs.entity.cell.CellEcs
import com.silentgames.silent_planet.logic.ecs.entity.cell.DeathCell
import com.silentgames.silent_planet.logic.ecs.entity.cell.EmptyCell
import com.silentgames.silent_planet.logic.ecs.entity.cell.SpaceCell
import com.silentgames.silent_planet.logic.ecs.entity.cell.arrow.ArrowGreenCell
import com.silentgames.silent_planet.logic.ecs.entity.cell.arrow.ArrowRedCell
import com.silentgames.silent_planet.logic.ecs.entity.cell.crystal.CrystalCell
import com.silentgames.silent_planet.logic.ecs.entity.cell.crystal.CrystalsEnum
import java.util.*

class CellRandomGenerator(val context: Context) {

    private val random = Random()
    private val randomList = mutableListOf<RandomEntity>()

    fun generateBattleGround(
            cellGeneratorParams: CellGeneratorParams = CellGeneratorParams()
    ): List<CellEcs> {
        randomList.clear()
        randomList.addAll(cellGeneratorParams.getRandomEntityList())

        val vCountOfCells = Constants.verticalCountOfCells
        val hCountOfCells = Constants.horizontalCountOfCells

        val randomCellTypeList = MutableList(Constants.countOfGroundCells) {
            randomizeCell()
        }

        randomCellTypeList.shuffle()
        var count = -1

        val listCells = mutableListOf<CellEcs>()

        for (x in 0 until hCountOfCells) {
            for (y in 0 until vCountOfCells) {
                if (x == 0 || x == hCountOfCells - 1 || y == 0 || y == vCountOfCells - 1) {
                    listCells.add(
                            SpaceCell(
                                    context,
                                    Axis(x, y)
                            )
                    )
                } else {
                    count++
                    listCells.add(
                            randomCellTypeList[count].getCellType(Axis(x, y))
                    )
                }
            }
        }

        return listCells
    }


    private fun RandomCellType.getCellType(axis: Axis): CellEcs {
        return when (this) {
            RandomCellType.DEATH -> DeathCell(context, axis)
            RandomCellType.GREEN_ARROW -> ArrowGreenCell(context, axis, RotateAngle.randomAngle())
            RandomCellType.RED_ARROW -> ArrowRedCell(context, axis, RotateAngle.randomAngle())
            RandomCellType.CRYSTAL_ONE -> CrystalCell(context, axis, CrystalsEnum.ONE)
            RandomCellType.CRYSTAL_TWO -> CrystalCell(context, axis, CrystalsEnum.TWO)
            RandomCellType.CRYSTAL_THREE -> CrystalCell(context, axis, CrystalsEnum.THREE)
            RandomCellType.EMPTY -> EmptyCell(context, axis)
        }
    }

    private fun randomizeCell(): RandomCellType {
        return if (randomList.size > 0) {
            val pair = randomList.getMaxChance()
            val index = random.randChance(pair.first, pair.second, randomList.size)
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