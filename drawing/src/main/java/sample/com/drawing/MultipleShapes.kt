package sample.com.drawing

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import sample.com.drawing.shape.Shape
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
    var selectedValue: Shape? = null

    fun getShape(id: Long): Shape? {
        return shapeManager.get(id)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val value = selectedValue
        if (value != null && value.handleTouch(event)) {
//            Log.d(TAG, "Event has been consumed")
            return true
        } else {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    downY = event.rawY
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = event.rawX - downX
                    val deltaY = event.rawY - downY

                    if (deltaX.absoluteValue < delta && deltaY.absoluteValue < delta && shapeManager.isNotEmpty()) {
                        shapeManager.firstOrNull { it.containsUsingMatrix(event.x, event.y) }
                                .apply {
                                    selectedValue?.select(false)
                                    selectedValue = this
                                    this?.select(true)
                                    return true
                                }
                    }
                    selectedValue?.select(false)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {
        const val TAG = "MultipleShapes"
    }
}