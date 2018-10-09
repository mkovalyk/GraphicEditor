package sample.com.drawing.shape

import android.graphics.*
import android.util.Log
import android.view.View

/**
 * Created on 09.10.18.
 */
open class Oval(container: View, val center: PointF, val radius: Float, id: Long) : Shape(id, container) {

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
        path.addOval(borders, Path.Direction.CW)
        Log.d("Circle", "Center: $center. Radius: $radius")
    }

    override fun onBordersChanged(borders: RectF) {
        path.reset()
        path.addOval(borders, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}