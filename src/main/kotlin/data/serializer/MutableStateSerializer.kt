package data.serializer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

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