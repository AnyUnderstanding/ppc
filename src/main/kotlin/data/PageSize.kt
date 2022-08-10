package data

import util.Point

enum class PageSize(val width: Float, val height: Float) {
    A4(210f*3, 297f*3), A5(148f, 210f);

    fun toPoint(): Point = Point(width.toDouble(), height.toDouble())
}