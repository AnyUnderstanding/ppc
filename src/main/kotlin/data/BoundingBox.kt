package data

import androidx.compose.runtime.MutableState
import util.Point
import kotlin.math.abs

@kotlinx.serialization.Serializable
data class BoundingBox(val point0: Point, val point1: Point) {
    val left by lazy { if (point0.x <= point1.x) point0.x else point1.x }
    val right by lazy { if (point0.x >= point1.x) point0.x else point1.x }
    val top by lazy { if (point0.y >= point1.y) point0.y else point1.y }
    val bottom by lazy { if (point0.y <= point1.y) point0.y else point1.y }
    val width by lazy { right - left }
    val height by lazy { top - bottom  }

    operator fun contains(point: Point): Boolean = point.x in left..right && point.y in bottom..top


    operator fun contains(other: BoundingBox): Boolean = (abs((left + width/2) - (other.left + other.width/2)) * 2 < (width + other.width)) &&
                                                         (abs((bottom + height/2) - (other.bottom + other.height/2)) * 2 < (height + other.height))



}