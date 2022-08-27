package data

import androidx.compose.runtime.mutableStateListOf
import util.Point
import androidx.compose.ui.graphics.Color
import data.serializer.SnapshotListSerializer

// Stroke
@kotlinx.serialization.Serializable
data class Page(var pageType: PageType, val topLeft: Point) : StrokeHolder {
    @kotlinx.serialization.Serializable(with = SnapshotListSerializer::class)
    val strokes = mutableStateListOf<Stroke>()


    override fun addStroke(color: Color, width: Float): Stroke {
        strokes.add(Stroke(color, width))
        return strokes.last()
    }

    override fun addStrokes(strokesToAdd: List<Stroke>) {
        strokes.addAll(strokesToAdd)
    }

    override fun removeStrokes(strokesToDelete: List<Stroke>) {
        strokes.removeAll(strokesToDelete)
    }


}