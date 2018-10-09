package sample.com.drawing.shape

import android.graphics.*
import android.util.Log
import android.view.View
import kotlin.math.min

/**
 * Created on 01.10.18.
 */
open class Circle(container: View, val center: PointF, val radius: Float, id: Long) : Shape(id, container) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    init {
        val x = center.x - radius - paint.strokeWidth / 2
        val y = center.y - radius - paint.strokeWidth / 2
        val width = radius * 2 + paint.strokeWidth
        val height = radius * 2 + paint.strokeWidth
        setBorders(x, y, x + width, y + height)
        path.reset()
        path.addCircle(center.x, center.y, radius, Path.Direction.CW)
        Log.d("Circle", "Center: $center. Radius: $radius")
    }

    override fun onBordersChanged(borders: RectF) {
        path.reset()
        path.addCircle(borders.centerX(), borders.centerY(), min(borders.width(), borders.height()) / 2, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}