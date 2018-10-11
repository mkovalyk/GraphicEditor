package sample.com.drawing.shape

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import sample.com.drawing.Boundary
import sample.com.drawing.State
import sample.com.drawing.toArray
import sample.com.drawing.toDegree
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min


/**
 * Created on 01.10.18.
 */
abstract class Shape(val id: Long, var container: View? = null) {

    protected val borders: RectF = RectF()
    var state = State.Default
        protected set(value) {
            field = value
            Log.d(TAG, "state Changed: $value")
            invalidate()
        }
    var matrix: Matrix = Matrix()
    var rotation = 0f
        protected set
    var translationX: Float = 0f
        protected set
    var translationY: Float = 0f
        protected set

    protected val path = Path()
    protected val resizeIndicator = RectF()
    protected val rotateIndicator = RectF()

    private val resizePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 2f
        style = Paint.Style.FILL
    }

    private val rotatePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 2f
        style = Paint.Style.FILL
    }

    private val resizeTouchedPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 2f
        style = Paint.Style.FILL
    }

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var originAngle = 0f

    fun setBorders(left: Float, top: Float, right: Float, bottom: Float) {
        borders.set(left, top, right, bottom)
        onBordersChanged(borders)
        val width = borders.width()
        val height = borders.height()
        val offset = min(width * 0.4f, height * 0.4f)
        val minOffset = min(offset, 100f)
        resizeIndicator.set(right - minOffset, bottom - minOffset, right, bottom)
        rotateIndicator.set(left, top, left + minOffset, top + minOffset)
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

    fun rotateBy(angle: Float) {
        Log.d(TAG, "rotateBy:$angle")
        rotation += angle
        val centerPoint = floatArrayOf(borders.centerX(), borders.centerY())
        matrix.mapPoints(centerPoint)
        matrix.postRotate(angle, centerPoint[0], centerPoint[1])
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
            translationX += deltaX
            translationY += deltaY
            Log.d(TAG, "move:$translationX, $translationY")
            matrix.postTranslate(deltaX, deltaY)
        }
        invalidate()
    }

    open fun contains(x: Float, y: Float): Boolean {
        Log.d(TAG, "contains: [$x: $y]. This:$this")
        return borders.contains(x, y)
    }

    open fun handleTouch(event: MotionEvent): Boolean {
        if (state == State.Selected) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (containsUsingMatrix(resizeIndicator, event.x, event.y)) {
                    enterResizingMode(event)
                    return true
                } else
                    if (containsUsingMatrix(rotateIndicator, event.x, event.y)) {
                        enterRotatingMode(event)
                        return true
                    } else
                        if (containsUsingMatrix(borders, event.x, event.y)) {
                            enterDraggingMode(event)
                            return true
                        } else {
                            state = State.Default
                            return false
                        }
            }
        } else if (state == State.Resizing) {
            return handleResizing(event)
        } else if (state == State.Dragging) {
            return handleDragging(event)
        } else if (state == State.Rotating) {
            return handleRotating(event)
        }
        return false
    }

    private fun enterDraggingMode(event: MotionEvent) {
        downX = event.rawX
        downY = event.rawY
        state = State.Dragging
    }

    private fun enterRotatingMode(event: MotionEvent) {
        val centerPoint = floatArrayOf(borders.centerX(), borders.centerY())
        matrix.mapPoints(centerPoint)
        downX = event.x
        downY = event.y
        state = State.Rotating
        originAngle = atan2(event.y - centerPoint[1], event.x - centerPoint[0]).toDegree()
    }

    private fun enterResizingMode(event: MotionEvent) {
        val startPoint = floatArrayOf(event.x, event.y)
        matrix.mapPoints(startPoint)
        downX = startPoint[0]
        downY = startPoint[1]
        state = State.Resizing
    }

    private fun handleRotating(event: MotionEvent): Boolean {
        when (event.action) {

            MotionEvent.ACTION_MOVE -> {
                val centerPoint = floatArrayOf(borders.centerX(), borders.centerY())
                matrix.mapPoints(centerPoint)
                val delta = atan2(event.y - centerPoint[1], event.x - centerPoint[0]).toDegree() - originAngle

                if (abs(delta) > 1) {
                    rotateBy(delta)
                    originAngle += delta
                }
                return true
            }

            MotionEvent.ACTION_UP -> {
                state = State.Selected
                return true
            }
        }
        return false
    }

    private fun handleResizing(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val points = floatArrayOf(event.x, event.y)
                matrix.mapPoints(points)
                val deltaX = points[0] - downX
                val deltaY = points[1] - downY

                downX = points[0]
                downY = points[1]
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
//                Log.d(TAG, "handleDragging. State: $state Event: $event")
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
        return containsUsingMatrix(borders, x, y)
    }

    private var latestClickedPoint = PointF()

    private fun containsUsingMatrix(rect: RectF, x: Float, y: Float): Boolean {
        Log.d(TAG, "containsMatrix:[$x, $y ]. Rectangle: $rect")
        val mappedValue = floatArrayOf(x, y)
        val values = rect.toArray()
        val result = FloatArray(values.size)

        matrix.mapPoints(result, values)
        val boundary = Boundary.fromArray(result)
        val contains = boundary.contains(PointF(mappedValue[0], mappedValue[1]))

        Log.d(TAG, "containsMatrix: [${mappedValue[0]}, ${mappedValue[1]}]. Boundary: $boundary Result: $contains")
//        matrix.mapPoints(mappedValue)
        latestClickedPoint.x = mappedValue[0]
        latestClickedPoint.y = mappedValue[1]

//        Log.d(TAG, "Mapped values: $x -> ${mappedValue[0]}, $y -> ${mappedValue[1]}. ")
        return contains
    }

    fun draw(canvas: Canvas) {
        val count = canvas.save()
        canvas.matrix = matrix
        if (state == State.Selected || state == State.Dragging || state == State.Rotating) {
            canvas.drawRect(borders, selectedPaint)
            canvas.drawRect(resizeIndicator, resizePaint)
            canvas.drawRect(rotateIndicator, rotatePaint)
        } else if (state == State.Resizing) {
            canvas.drawRect(borders, selectedPaint)
            canvas.drawRect(resizeIndicator, resizeTouchedPaint)
        }
// else if (state == State.Rotating) {
//            canvas.drawRect(borders, selectedPaint)
//            canvas.drawRect(resizeIndicator, rotatePaint)
//        }
        onDraw(canvas)
        canvas.restoreToCount(count)

//        val finalRectangle = RectF()
//        matrix.mapRect(finalRectangle, borders)
//        canvas.drawRect(finalRectangle, mappedPaint)

//        canvas.drawCircle(latestClickedPoint.x, latestClickedPoint.y, 10f, resizePaint)

    }

    protected abstract fun onDraw(canvas: Canvas)

    fun invalidate() {
        container?.invalidate()
    }


    override fun toString(): String {
        return "Shape($borders, selected=$state)"
    }

    companion object {
        const val TAG = "Shape"
    }
}