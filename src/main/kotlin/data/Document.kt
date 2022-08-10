package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
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
    // todo: serialization
    var selectedTool = mutableStateOf(Tool.Pen)

    @Transient
    var selectedColor: Color = Color.Red
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

class SnapshotListSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<SnapshotStateList<T>> {

    override val descriptor: SerialDescriptor = ListSerializer(dataSerializer).descriptor

    override fun serialize(encoder: Encoder, value: SnapshotStateList<T>) {
        encoder.encodeSerializableValue(ListSerializer(dataSerializer), value as List<T>)
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<T> {
        val list = mutableStateListOf<T>()
        val items = decoder.decodeSerializableValue(ListSerializer(dataSerializer))
        list.addAll(items)
        return list
    }
}

class MutableStateSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<MutableState<T>> {
    override val descriptor: SerialDescriptor = dataSerializer.descriptor

    override fun serialize(encoder: Encoder, value: MutableState<T>) {
        encoder.encodeSerializableValue(dataSerializer, value.value)
    }

    override fun deserialize(decoder: Decoder): MutableState<T> {
        val value = decoder.decodeSerializableValue(dataSerializer)
        return mutableStateOf(value)
    }
}