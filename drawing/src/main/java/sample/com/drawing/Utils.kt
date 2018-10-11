package sample.com.drawing

import android.graphics.RectF
import kotlin.math.PI

/**
 * Created on 09.10.18.
 */
fun RectF.toArray(): FloatArray {
    return floatArrayOf(left, top, right, top, right, bottom, left, bottom)
}

fun Float.toRadians(): Float {
    return this / 180.0f * PI.toFloat()
}

fun Float.toDegree(): Float {
    return this * 180f / PI.toFloat()
}