package util

import androidx.compose.ui.geometry.Offset
import kotlin.math.pow
import kotlin.math.sqrt

@kotlinx.serialization.Serializable
data class Point(
    val x: Double, val y: Double
) {
    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())
    constructor(x: Double, y: Int) : this(x, y.toDouble())
    constructor(x: Int, y: Double) : this(x.toDouble(), y)


    operator fun unaryMinus() = Point(-x, -y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)

    operator fun times(other: Point) = Point(x * other.x, y * other.y)
    operator fun times(other: Double) = Point(x * other, y * other)
    operator fun times(other: Float) = Point(x * other, y * other)
    operator fun times(other: Int) = Point(x * other, y * other)

    fun getDistance(vec: Point, alpha: Double): Double {
        return ((vec.x - x).pow(2) + (vec.y - y).pow(2)).pow(alpha)
    }

    val length: Double
        get() = sqrt(x.pow(2) + y.pow(2))


    fun cross(other: Point): Double {
        return x * other.y - y * other.x
    }



    fun toOffset(): Offset = Offset(x.toFloat(), y.toFloat())
}