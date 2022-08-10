package util

class PointList(val values: List<Point>) {
    operator fun plus(other: PointList) = PointList(values.zip(other.values).map {it.first + it.second}.toList())
    operator fun times(other: ScalarList) = PointList(values.zip(other.values).map {it.first * it.second}.toList())
    operator fun times(other: Double) = PointList(values.map {it * other}.toList())

}