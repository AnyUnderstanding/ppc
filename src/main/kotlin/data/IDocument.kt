package data

import androidx.compose.runtime.mutableStateOf
import data.serializer.MutableStateSerializer
import util.Point

abstract class IDocument {
    var scrollX = 0.0f
    var scrollY = 0.0f
    var zoomFactor = 1.0f
    val selectedStroke: Stroke? = null

    @kotlinx.serialization.Serializable(with = MutableStateSerializer::class)
    var centerPoint = mutableStateOf(Point(0, 0))


    fun updateZoom(zoomDelta: Float, centerPoint: Point) {
        zoomFactor += zoomDelta // Note: this must be called first since changing the zoomFactor first will cause an immediate recompose resulting in an incomplete since the centerPoint would not been set
        this.centerPoint.value = centerPoint
    }
}