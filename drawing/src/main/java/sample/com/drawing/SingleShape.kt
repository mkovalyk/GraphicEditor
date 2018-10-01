package sample.com.drawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created on 01.10.18.
 */
class SingleShape @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }
    val random = Random()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(random.nextInt(100).toFloat(),
                random.nextInt(100).toFloat(),
                random.nextInt(100).toFloat(),
                paint)
    }
}