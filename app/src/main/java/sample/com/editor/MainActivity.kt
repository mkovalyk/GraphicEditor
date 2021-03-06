package sample.com.editor

import android.animation.ObjectAnimator
import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import sample.com.drawing.Circle
import sample.com.drawing.MultipleShapes
import sample.com.drawing.operation.AddShapeOperation
import sample.com.drawing.operation.OperationManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var multipleShapes: MultipleShapes
    private val random = Random()
    val mainLayout by lazy { findViewById<FrameLayout>(R.id.main_layout) }
    private lateinit var circle: Circle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layout = findViewById<FrameLayout>(R.id.layout)

//        var startTime = System.nanoTime()
        multipleShapes = MultipleShapes(context = this)
        val layoutParams = FrameLayout.LayoutParams(1000, 1000)
        multipleShapes.layoutParams = layoutParams
        layoutParams.leftMargin = random.nextInt(100)
        layoutParams.topMargin = random.nextInt(100)
        mainLayout.addView(multipleShapes)

        circle = Circle(PointF(200f, 200f), 100f, System.currentTimeMillis()).apply {
            selected = true
        }
        manager.apply(AddShapeOperation(multipleShapes.shapeManager, circle))
        multipleShapes.invalidate()

//        multipleShapes.addShape(circle)
//
//        var endTime = System.nanoTime() - startTime
//        Log.d(TAG, "drawFinished: 1 multiple shapes${TimeUnit.NANOSECONDS.toMicros(endTime)} ")
//
//        startTime = System.nanoTime()
//        for (counter in 0..50) {
//            val view = SingleShape(context = this)
//            val layoutParams = FrameLayout.LayoutParams(200, 200)
//            view.layoutParams = layoutParams
//            layoutParams.leftMargin = random.nextInt(500)
//            layoutParams.topMargin = random.nextInt(900)
//            layout.addView(view)
//        }
//        endTime = System.nanoTime() - startTime
//        Log.d(TAG, "drawFinished: 50 Single shapes ${TimeUnit.NANOSECONDS.toMicros(endTime)} ")
//
//        Handler().postDelayed({
//            startTime = System.nanoTime()
//            layout.requestLayout()
//            endTime = System.nanoTime() - startTime
//            Log.d(TAG, "drawFinished: RequestLayout ${TimeUnit.NANOSECONDS.toMicros(endTime)} ")
//
//            startTime = System.nanoTime()
//            mainLayout.requestLayout()
//            endTime = System.nanoTime() - startTime
//            Log.d(TAG, "drawFinished: Main layout ${TimeUnit.NANOSECONDS.toMicros(endTime)} ")
//
//        },1000)
    }

    val manager = OperationManager()

    fun onClickButton(view: View) {
        when (view.id) {
            R.id.button -> {
                val x = random.nextInt(400).toFloat()
                val y = random.nextInt(400).toFloat()
                val radius = random.nextInt(400).toFloat()
                circle = Circle(PointF(x, y), radius, System.currentTimeMillis())
                manager.apply(AddShapeOperation(multipleShapes.shapeManager, circle))
                multipleShapes.invalidate()
            }
            R.id.animate -> {
                val animator = ObjectAnimator.ofFloat(circle, "x", 300f)
                animator.duration = 200
                animator.start()
                animator.addUpdateListener {
                    multipleShapes.invalidate()
                }
            }
            R.id.undo -> {
                manager.undo {
                    multipleShapes.invalidate()
                }
            }
            R.id.redo -> {
                manager.redo {
                    multipleShapes.invalidate()
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
