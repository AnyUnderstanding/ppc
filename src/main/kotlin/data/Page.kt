package data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Contextual
import util.Point
import java.util.UUID

// Stroke
@kotlinx.serialization.Serializable
data class Page(var pageType: PageType, val topLeft: Point, val uuid: String = UUID.randomUUID().toString()) {
    @kotlinx.serialization.Serializable(with = SnapshotListSerializer::class)
//    @Transient
//    @Contextual
    val strokes = mutableStateListOf<Stroke>()



    fun newStroke(color: ULong, uuid: String = ""): Stroke {
        if (uuid.isBlank())
            strokes.add(Stroke(color))
        else
            strokes.add(Stroke(color,uuid = uuid))
        return strokes.last()
    }




}