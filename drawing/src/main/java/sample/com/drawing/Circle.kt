package sample.com.drawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import kotlin.math.min

/**
 * Created on 01.10.18.
 */
open class Circle(center: PointF, radius: Float) : Shape() {

    init {
        x = center.x - radius
        y = center.y - radius
        width = radius * 2
        height = radius * 2
    }

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x + width / 2, y + height / 2, min(width / 2, height / 2), paint)
        super.draw(canvas)
    }
}