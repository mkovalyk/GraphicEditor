package sample.com.drawing.shape

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

/**
 * Created on 08.10.18.
 */
class Rectangle(container: View, id: Long, borders: RectF) : Shape(id, container) {
    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(borders, paint)
    }

    init {
        this.setBorders(borders.left, borders.top, borders.right, borders.bottom)
    }
}