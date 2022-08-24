package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import util.Point
import util.calcBoundingBox

class Selection(var start: Point) {

    private var isCompleted: Boolean = false
        get() = field

    var end: MutableState<Point?> = mutableStateOf(null)
    var selectedStrokes = mutableListOf<Stroke>()
    var strokeBoundingBox: BoundingBox? = null
    var selectionComplete = false



    fun addStroke(strokes: List<Stroke>) {
        // selectedStrokes = strokes.toMutableList()
        selectedStrokes.clear()
        selectedStrokes.addAll(strokes)
//        println("ss: ${selectedStrokes.size}")
        if (strokes.isEmpty() || start == end.value) {
            strokeBoundingBox = null
            return
        }

        strokeBoundingBox =
            calcBoundingBox(
                strokes.map { it.mainBoundingBox.point0 }
                    .zip(strokes.map { it.mainBoundingBox.point1 }) { a, b -> listOf(a, b) }.flatten()
            )
    }

    fun complete() {
        var left = Double.POSITIVE_INFINITY
        var right = Double.NEGATIVE_INFINITY
        var top = Double.POSITIVE_INFINITY
        var bottom = Double.NEGATIVE_INFINITY
        isCompleted = true

        selectedStrokes.flatMap { it.boundingBoxes }.forEach {
            if (it.left < left) left = it.left
            if (it.right > right) right = it.right
            if (it.top < top) top = it.top
            if (it.bottom > bottom) bottom = it.bottom
        }

        start = Point(left, top)
        end.value = Point(right, bottom)
    }
}