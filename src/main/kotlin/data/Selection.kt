package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import util.Point

class Selection(var start: Point) {

    private var isCompleted: Boolean = false
       get() = isCompleted

    var end: MutableState<Point?> = mutableStateOf(null)

    val selectedStrokes = mutableListOf<Stroke>()

    fun complete() {
        var left = Double.POSITIVE_INFINITY
        var right = Double.NEGATIVE_INFINITY
        var top =  Double.POSITIVE_INFINITY
        var bottom = Double.NEGATIVE_INFINITY
        isCompleted = true

        selectedStrokes.flatMap { it.boundingBoxes }.forEach{
            if (it.left < left) left = it.left
            if (it.right > right) right = it.right
            if (it.top < top) top = it.top
            if (it.bottom > bottom) bottom = it.bottom
        }

        start = Point(left, top)
        end.value = Point(right, bottom)
    }



}