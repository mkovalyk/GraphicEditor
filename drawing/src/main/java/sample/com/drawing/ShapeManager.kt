package sample.com.drawing

import sample.com.drawing.shape.Shape

/**
 * Created on 02.10.18.
 */
class ShapeManager {
    private val shapes = sortedMapOf<Long, Shape>()
    fun add(shape: Shape) {
        shapes[shape.id] = shape
    }

    fun get(id: Long): Shape? {
        return shapes[id]
    }

    fun remove(id: Long) {
        shapes.remove(id)
    }

    fun isNotEmpty(): Boolean {
        return shapes.isNotEmpty()
    }

    fun forEach(action: (Shape) -> Unit) {
        shapes.forEach {
            action.invoke(it.value)
        }
    }

    fun firstOrNull(predicate: (Shape) -> Boolean): Shape? {
        // It is not performance-perfect to iterate until last is found. Will try to find better
        // solution in future
        return shapes.values.lastOrNull(predicate)
    }
}

