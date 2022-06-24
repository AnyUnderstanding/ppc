package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import util.Point
import java.io.File
import java.util.UUID

// Point, Page
@Serializable
data class Document(val pageSize: PageSize) {

    @Serializable(with = MutableStateSerializer::class)
    var selectedTool = mutableStateOf( Tool.Pen )
    var selectedColor: ULong = 0xFF0000FFU
    var scrollX = 0.0f
    var scrollY = 0.0f
    var zoomFactor = 1.0f
        // changing this should not cause a recompose because change will occur in combination with the zoomFactor
        private set

    //    @Transient
    @Serializable(with = MutableStateSerializer::class)
    var centerPoint = mutableStateOf(Point(0, 0))

    @Serializable(with = SnapshotListSerializer::class)
//    @Transient
//    @Contextual
    val pages = mutableStateListOf<Page>()

    val pageCount
        get() = pages.size

    fun setZoom(zoomDelta: Float, centerPoint: Point) {
        zoomFactor += zoomDelta // Note: this must be called first since changing the zoomFactor first will cause an immediate recompose resulting in an incomplete since the centerPoint would not been set
        this.centerPoint.value = centerPoint
    }

    fun newPage(pageType: PageType, topLeft: Point, uuid: String = "") {
        if (uuid.isBlank())
            pages.add(Page(pageType, topLeft))
        else
            pages.add(Page(pageType, topLeft, uuid = uuid))
    }

    //@OptIn(DelicateCoroutinesApi::class)
    fun toJSON(): String {
        return Json.encodeToString(this)
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