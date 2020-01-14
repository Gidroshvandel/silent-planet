package com.silentgames.silent_planet.engine.base

import android.graphics.Paint
import java.util.*


open class Layer {

    var data = LinkedList<Basic>()

    /**
     * Кисть, которой должен отрисовываться слой
     */
    var paint: Paint

    private var processing = false

    init {
        processing = true
        paint = Paint()
        processing = false
    }

    /**
     * добавление на слой объектов
     *
     * @param item
     * - графический объект
     */
    fun add(item: Basic) {
        processing = true
        data.add(item)
        processing = false
    }

    /**
     * @return возвращает количество объектов на слое
     */
    fun getSize(): Int {
        return data.size
    }

    /**
     * Возвращает объект **Basic** c номером **i**
     *
     * @param i
     * номер объекта
     * @return объект со слоя с номером **i**
     */
    operator fun get(i: Int): Basic {
        return data[i]
    }

    /**
     * Удаляет объект с номером i со слоя
     *
     * @param i
     */
    fun delete(i: Int) {
        processing = true
        data.removeAt(i)
        processing = false
    }

    /**
     * Очищает текущий солой
     */
    fun clear() {
        processing = true
        data.clear()
        processing = false
    }

    /**
     * обновляет все объекты на слое
     */
    fun update(onUpdated: ((Boolean) -> Unit)) {
        var updatedDataCount = 0
        val dataSize = data.size
        var somethingChange = false
        processing = true
        data.forEach {
            it.update { changed ->
                updatedDataCount++
                if (changed) {
                    somethingChange = true
                }
                if (dataSize == updatedDataCount) {
                    processing = false
                    onUpdated.invoke(somethingChange)
                }
            }
        }
    }

    fun isProcessing(): Boolean {
        return processing
    }

}