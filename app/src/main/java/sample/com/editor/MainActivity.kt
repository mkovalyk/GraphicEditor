package sample.com.editor

import android.graphics.PointF
import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import sample.com.drawing.MultipleShapes
import sample.com.drawing.operation.AddShapeOperation
import sample.com.drawing.operation.OperationManager
import sample.com.drawing.shape.Circle
import sample.com.drawing.shape.Rectangle
import sample.com.drawing.shape.Shape
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var multipleShapes: MultipleShapes
    private val random = Random()
    val mainLayout by lazy { findViewById<FrameLayout>(R.id.main_layout) }
    private lateinit var circle: Shape


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        multipleShapes = MultipleShapes(context = this)
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        multipleShapes.layoutParams = layoutParams
        layoutParams.leftMargin = random.nextInt(100)
        layoutParams.topMargin = random.nextInt(100)
        mainLayout.addView(multipleShapes)

        circle = Circle(multipleShapes, PointF(200f, 200f), 100f, System.currentTimeMillis()).apply {
            select(true)
        }
        manager.perform(AddShapeOperation(multipleShapes.shapeManager, circle))
        multipleShapes.invalidate()
    }

    val manager = OperationManager()

    fun onClickButton(view: View) {
        when (view.id) {
            R.id.button -> {
                val x = random.nextInt(400).toFloat()
                val y = random.nextInt(400).toFloat()
                val radius = random.nextInt(400).toFloat()
                circle = Rectangle(multipleShapes, System.currentTimeMillis(), RectF(x, y, x + radius, y + radius))
                manager.perform(AddShapeOperation(multipleShapes.shapeManager, circle))
                multipleShapes.invalidate()
            }
            R.id.animate -> {
                multipleShapes.selectedValue?.rotateBy(45f)
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
