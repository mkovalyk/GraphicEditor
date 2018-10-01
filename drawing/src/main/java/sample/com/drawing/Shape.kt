package sample.com.drawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

/**
 * Created on 01.10.18.
 */
abstract class Shape {
    var x = 0f
        protected set
    var y = 0f
        protected set

    var width = 0f
        protected set
    var height = 0f
        protected set

    val selectedPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    var selected: Boolean = false

    fun rotate(angle: Float) {
    }

    open fun draw(canvas: Canvas) {
        if (selected) {
            canvas.drawRect(x, y, x + width, y + height, selectedPaint)
        }
    }
}