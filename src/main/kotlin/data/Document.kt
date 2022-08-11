package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import data.serializer.MutableStateSerializer
import data.serializer.SnapshotListSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import util.Point

// Point, Page
@kotlinx.serialization.Serializable
class Document(val pageSize: PageSize) {


    var scrollX = 0.0f
    var scrollY = 0.0f
    var zoomFactor = 1.0f
        // changing this should not cause a recompose because change will occur in combination with the zoomFactor
        private set

    @kotlinx.serialization.Serializable(with = MutableStateSerializer::class)
    var centerPoint = mutableStateOf(Point(0, 0))

    @kotlinx.serialization.Serializable(with = SnapshotListSerializer::class)
    val pages = mutableStateListOf<Page>()

    val pageCount
        get() = pages.size

    fun updateZoom(zoomDelta: Float, centerPoint: Point) {
        zoomFactor += zoomDelta // Note: this must be called first since changing the zoomFactor first will cause an immediate recompose resulting in an incomplete since the centerPoint would not been set
        this.centerPoint.value = centerPoint
    }

    fun setZoom(factor: Float, centerPoint: Point) {
        zoomFactor = factor
        this.centerPoint.value = centerPoint
    }

    fun newPage(pageType: PageType, topLeft: Point) {
        pages.add(Page(pageType, topLeft))
    }

    fun getPage(i: Int): Page = pages[i]
}