package data

import androidx.compose.runtime.mutableStateListOf
import util.Point
import androidx.compose.ui.graphics.Color
import data.serializer.SnapshotListSerializer

// Stroke
@kotlinx.serialization.Serializable
data class Page(var pageType: PageType, val topLeft: Point) {
    @kotlinx.serialization.Serializable(with = SnapshotListSerializer::class)
    val strokes = mutableStateListOf<Stroke>()

    fun newStroke(color: Color): Stroke {
        strokes.add(Stroke(color))
        return strokes.last()
    }
}