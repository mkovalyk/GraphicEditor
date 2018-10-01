package sample.com.drawing

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created on 01.10.18.
 */
class MultipleShapes @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
//    val paint = Paint().apply {
//        color = Color.RED
//        strokeWidth = 10f
//        style = Paint.Style.STROKE
//    }
//    val random = Random()

    private val shapes = mutableListOf<Shape>()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (shape in shapes) {
            shape.draw(canvas)
        }
//        val startTime = System.nanoTime()
//        for (counter in 0..50) {
//            canvas.drawCircle(random.nextInt(1000).toFloat(),
//                    random.nextInt(1000).toFloat(),
//                    random.nextInt(100).toFloat(),
//                    paint)
//        }
//        Log.d(TAG, "drawFinished: ${(System.nanoTime() - startTime).div(1000)} ")
    }

    fun addShape(shape: Shape) {
        shapes.add(shape)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    companion object {
        const val TAG = "MultipleShapes"
    }
}