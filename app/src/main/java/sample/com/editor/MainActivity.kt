package sample.com.editor

import android.graphics.PointF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import sample.com.drawing.Circle
import sample.com.drawing.MultipleShapes
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var multipleShapes: MultipleShapes
    private val random = Random()
    val mainLayout by lazy { findViewById<FrameLayout>(R.id.main_layout) }


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
        val circle = Circle(PointF(200f, 200f), 100f).apply {
            selected = true
        }
        multipleShapes.addShape(circle)
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


    fun onClickButton(view: View) {
        val x = random.nextInt(400).toFloat()
        val y = random.nextInt(400).toFloat()
        val radius = random.nextInt(400).toFloat()
        multipleShapes.addShape(Circle(PointF(x, y), radius))
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
