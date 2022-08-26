package data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.serializer.MutableStateSerializer
import util.Point

class InfiniteDocument : IDocument() {

    val chunks: MutableMap<Int, MutableMap<Int, Chunk>> = mutableMapOf()
    val chunkSize = Point(100,100)



}