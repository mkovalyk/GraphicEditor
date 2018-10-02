package sample.com.drawing

import android.graphics.*
import android.util.Log


/**
 * Created on 01.10.18.
 */
abstract class Shape(val id: Long) {
    var x = 0f
        protected set
    var y = 0f
        protected set

    var width = 0f
        protected set
    var height = 0f
        protected set
    protected val path = Path()

    val selectedPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        style = Paint.Style.STROKE
    }

    var selected: Boolean = false

    fun rotate(angle: Float) {
    }

    open fun include(x: Float, y: Float): Boolean {
        Log.d("Shape", "include: [$x: $y]. This:$this")
        return x >= this.x && y >= this.y &&
                x <= this.x + width && y <= this.y + height
    }

    open fun draw(canvas: Canvas) {
        if (selected) {
            canvas.drawRect(x, y, x + width, y + height, selectedPaint)
        }
    }

    override fun toString(): String {
        return "Shape(x=$x, y=$y, width=$width, height=$height, selected=$selected)"
    }
}