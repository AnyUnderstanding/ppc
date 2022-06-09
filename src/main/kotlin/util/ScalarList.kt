package util

class ScalarList(val values: List<Double>) {


    operator fun unaryMinus() = ScalarList(values.map { -it }.toList())
    operator fun plus(other: Double) = ScalarList(values.map { it + other }.toList())
    operator fun minus(other: Double) = ScalarList(values.map { it - other }.toList())
    operator fun times(other: Point) = PointList(values.map { other * it })
    operator fun div(other: Double) = ScalarList(values.map { it / other }.toList())
}