package com.silentgames.silent_planet.logic

import android.support.annotation.IntRange
import java.util.*

/**
 * value - значение которое должно часто выпадать
 * chance - шанс его выпадения в процентах от 0 до 100
 * max - ограничитель рандумных чисел
 *
 * @return от нуля, включительно, до max не включительно
 */
fun Random.randChance(value: Int, @IntRange(from = 0, to = 1000) chance: Int, max: Int): Int {
    if (chance < 0 || chance > 100) {
        throw IllegalArgumentException("Chance must be between 0 and 100")
    }
    val random = nextInt(100)
    return if (random < chance) {
        value
    } else nextInt(max)
    //Даже при нулевом шансе число всё-таки может выпасть ТУТ.
}