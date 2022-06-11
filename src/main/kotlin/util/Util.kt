package util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import data.BoundingBox
import java.io.FileInputStream


fun calcBoundingBox(points: List<Point>): BoundingBox {
    var left = Double.POSITIVE_INFINITY
    var right = Double.NEGATIVE_INFINITY
    var top = Double.POSITIVE_INFINITY
    var bottom = Double.NEGATIVE_INFINITY


    points.forEach { point ->
        if (point.x < left) left = point.x
        if (point.x > right) right = point.x
        if (point.y < top) top = point.y
        if (point.y > bottom) bottom = point.y
    }
    return BoundingBox(Point(left, top), Point(right, bottom))
}

