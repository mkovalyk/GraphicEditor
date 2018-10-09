package sample.com.drawing.shape

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import sample.com.drawing.State


/**
 * Created on 01.10.18.
 */
abstract class Shape(val id: Long, var container: View? = null) {

    protected val borders: RectF = RectF()
    var state = State.Default
        protected set(value) {
            field = value
            Log.d(TAG, "state Changed: $value")
        }
    var matrix: Matrix = Matrix()
    protected var rotation = 0f
    protected val path = Path()
    //    protected var container: View? = null
    protected val resizeIndicator = RectF()

    val resizePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 2f
        style = Paint.Style.FILL
    }

    fun setBorders(left: Float, top: Float, right: Float, bottom: Float) {
        borders.set(left, top, right, bottom)
        onBordersChanged(borders)
        val width = borders.width()
        val height = borders.height()
        resizeIndicator.set(left + width * 0.6f, top + height * 0.6f, left + width * 1.0f, top + height * 1.0f)
    }

    protected open fun onBordersChanged(borders: RectF) {
    }

    fun select(selected: Boolean) {
        if (state != State.Selected && selected) {
            state = State.Selected
        } else {
            state = State.Default
        }
    }

    val selectedPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        style = Paint.Style.STROKE
    }


    fun rotate(angle: Float) {
        rotation = angle
        matrix.setRotate(rotation)
        invalidate()
    }

    fun rotateBy(angle: Float) {
        rotation += angle
        matrix.setRotate(rotation, borders.centerX(), borders.centerY())
        invalidate()
    }

    fun resize(deltaX: Float, deltaY: Float) {
        Log.d(TAG, "resize: $deltaX, $deltaY")
        with(borders) {
            setBorders(left, top, right + deltaX, bottom + deltaY)
        }
        invalidate()
    }

    fun move(deltaX: Float, deltaY: Float) {
        with(borders) {
            setBorders(left + deltaX, top + deltaY, right + deltaX, bottom + deltaY)
        }
        invalidate()
    }

    open fun contains(x: Float, y: Float): Boolean {
        Log.d(TAG, "contains: [$x: $y]. This:$this")
        return borders.contains(x, y)
    }

    var downX: Float = 0f
    var downY: Float = 0f

    open fun handleTouch(event: MotionEvent): Boolean {
        if (state == State.Selected) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (containsUsingMatrix(resizeIndicator, event.x, event.y)) {
                        Log.d(TAG, "Start dragging: ")
                        downX = event.rawX
                        downY = event.rawY
                        state = State.Resizing
                        return true
                    } else if (containsUsingMatrix(borders, event.x, event.y)) {
                        downX = event.rawX
                        downY = event.rawY
                        state = State.Dragging
                        return true
                    } else {
                        state = State.Default
                        return false
                    }
                }
            }
        } else if (state == State.Resizing) {
            return handleResizing(event)
        } else if (state == State.Dragging) {
            return handleDragging(event)
        }
        return false
    }

    private fun handleResizing(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "handleResizing. State: $state Event: $event")
                val deltaX = event.rawX - downX
                val deltaY = event.rawY - downY
                downX = event.rawX
                downY = event.rawY
                resize(deltaX, deltaY)
                return true
            }
            MotionEvent.ACTION_UP -> {
                state = State.Selected
                return true
            }
        }
        return false
    }

    private fun handleDragging(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "handleDragging. State: $state Event: $event")
                val deltaX = event.rawX - downX
                val deltaY = event.rawY - downY
                downX = event.rawX
                downY = event.rawY
                move(deltaX, deltaY)
                return true
            }
            MotionEvent.ACTION_UP -> {
                state = State.Selected
                return true
            }
        }
        return false
    }

    fun containsUsingMatrix(x: Float, y: Float): Boolean {
        val mappedValue = floatArrayOf(x, y)
        matrix.mapPoints(mappedValue)
        val finalRectangle = RectF()
        matrix.mapRect(finalRectangle, borders)
        val contains = finalRectangle.contains(mappedValue[0], mappedValue[1])
        Log.d(TAG, "containsMatrix: [${mappedValue[0]}, ${mappedValue[1]}]. This:$finalRectangle. Result: $contains")
        return contains
    }

    private fun containsUsingMatrix(rect: RectF, x: Float, y: Float): Boolean {
        val mappedValue = floatArrayOf(x, y)
        matrix.mapPoints(mappedValue)
        val finalRectangle = RectF()
        matrix.mapRect(finalRectangle, rect)
        val contains = finalRectangle.contains(mappedValue[0], mappedValue[1])
        Log.d(TAG, "containsMatrix: [${mappedValue[0]}, ${mappedValue[1]}]. This:$finalRectangle. Result: $contains")
        return contains
    }

    fun draw(canvas: Canvas) {
        val count = canvas.save()
        canvas.matrix = matrix
        if (state == State.Selected || state == State.Dragging || state == State.Resizing) {
            canvas.drawRect(borders, selectedPaint)
            canvas.drawRect(resizeIndicator, resizePaint)
        }
        onDraw(canvas)
        canvas.restoreToCount(count)
    }

    protected abstract fun onDraw(canvas: Canvas)

    fun invalidate() {
        Log.d(TAG, "invalidate")
        container?.invalidate()
    }


    override fun toString(): String {
        return "Shape($borders, selected=$state)"
    }

    companion object {
        const val TAG = "Shape"
    }
}