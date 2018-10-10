package sample.com.drawing

import android.graphics.PointF
import android.util.Log


/**
 * Created on 09.10.18.
 */
class Boundary(val points: List<PointF>)// Points making up the boundary
{

    /**
     * Return true if the given point is contained inside the boundary.
     * See: http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     * @param test The point to check
     * @return true if the point is inside the boundary, false otherwise
     *
     */
    fun contains(test: PointF): Boolean {
        var i: Int = 0
        var j: Int
        var result = false
        j = points.size - 1
        while (i < points.size) {
            if (points[i].y > test.y != points[j].y > test.y && test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y) + points[i].x) {
                result = !result
            }
            j = i++
        }
        Log.d("Boundary", "Point $test. Result: $result")
        return result
    }


    override fun toString(): String {
        return points.joinToString(",", "Result:")
    }

    fun toArray(): FloatArray {
        val array = FloatArray(points.size * 2)
        points.forEachIndexed { index, point ->
            array[index] = point.x
            array[index + 1] = point.y
        }
        return array
    }

    companion object {
        fun fromArray(points: FloatArray): Boundary {
            val pointList = mutableListOf<PointF>()
            if (points.size % 2 != 0) {
                throw IllegalStateException("Input array can not be odd")
            }
            for (i in 0 until points.size step 2) {
                pointList.add(PointF(points[i], points[i + 1]))
            }
            return Boundary(pointList)
        }
    }
}