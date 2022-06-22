package data

import androidx.compose.runtime.mutableStateListOf
import util.Point

// Stroke
@kotlinx.serialization.Serializable
data class Page(var pageType: PageType, val topLeft: Point) {
    @kotlinx.serialization.Serializable(with = SnapshotListSerializer::class)
//    @Transient
//    @Contextual
    val strokes = mutableStateListOf<Stroke>()

    fun newStroke(color: ULong): Stroke {
        strokes.add(Stroke(color))
        return strokes.last()
    }


}