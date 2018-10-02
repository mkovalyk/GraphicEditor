package sample.com.drawing.operation

import sample.com.drawing.Shape
import sample.com.drawing.ShapeManager

/**
 * Created on 02.10.18.
 */
class AddShapeOperation(shapeManager: ShapeManager, val shape: Shape) : ShapeOperation(shapeManager) {
    override fun execute() {
        shapeManager.add(shape)
    }

    override fun revert() {
        shapeManager.remove(shape.id)
    }
}