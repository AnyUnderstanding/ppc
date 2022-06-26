package data

import androidx.compose.runtime.*
import control.DocumentController


class DocumentControlState(
    val documentController: DocumentController,
    doc: Document
) : WindowControlState {
    var document = mutableStateOf(doc)
    var connected = mutableStateOf(false)
    var sessionID: String = ""




//    var document by remember { mutableStateOf(doc) }
//        private set
//    init {
//        document = mutableStateOf(doc)
//    }
}