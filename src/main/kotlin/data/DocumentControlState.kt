package data

import androidx.compose.runtime.*
import control.DocumentController
import util.Point


class DocumentControlState(
    val documentController: DocumentController,
    doc: Document
) : WindowControlState {
    val strokes = mutableStateListOf<Stroke>()
    var document = mutableStateOf(doc)
        private set

//    var document by remember { mutableStateOf(doc) }
//        private set
//    init {
//        document = mutableStateOf(doc)
//    }
}