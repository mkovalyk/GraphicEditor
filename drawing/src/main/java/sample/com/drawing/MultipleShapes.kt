package sample.com.drawing

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.absoluteValue

/**
 * Created on 01.10.18.
 */
class MultipleShapes @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //    private val shapes = mutableListOf<Shape>()
    val shapeManager = ShapeManager()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        shapeManager.forEach {
            it.draw(canvas)
        }
    }

    fun addShape(shape: Shape) {
        shapeManager.add(shape)
        invalidate()
    }

    private var downX: Float = 0f
    private var downY: Float = 0f
    private val delta = 50
    private var selectedValue: Shape? = null

    fun getShape(id: Long): Shape? {
        return shapeManager.get(id)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "event: $event ")
                downX = event.rawX
                downY = event.rawY
                Log.d(TAG, "DOWN: [$downX : $downY]")
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "event: $event ")
                val deltaX = event.rawX - downX
                val deltaY = event.rawY - downY
                Log.d(TAG, "DELTA: [$deltaX : $deltaY]")

                if (deltaX.absoluteValue < delta && deltaY.absoluteValue < delta && shapeManager.isNotEmpty()) {
                    shapeManager.firstOrNull { it.include(event.x, event.y) }
                            .apply {
                                selectedValue?.selected = false
                                selectedValue = this
                                this?.selected = true
                                invalidate()
                            }
                    return true
                }
                selectedValue?.selected = false
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        const val TAG = "MultipleShapes"
    }
}