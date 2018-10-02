package sample.com.drawing

import android.graphics.*
import android.util.Log

/**
 * Created on 01.10.18.
 */
open class Circle(val center: PointF, val radius: Float, id: Long) : Shape(id) {


    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    init {
        x = center.x - radius - paint.strokeWidth / 2
        y = center.y - radius - paint.strokeWidth / 2
        width = radius * 2 + paint.strokeWidth
        height = radius * 2 + paint.strokeWidth
        path.reset()
        path.addCircle(center.x, center.y, radius, Path.Direction.CW)
        Log.d("Circle", "Center: $center. Radius: $radius")
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
        super.draw(canvas)
    }
}