package data

import androidx.compose.runtime.*
import control.DocumentControllerBase


class DocumentControlState(
    val documentController: DocumentControllerBase,
    doc: Document
) : WindowControlState {
    var document = mutableStateOf(doc)
        private set

//    override fun finalize() {}


//    var document by remember { mutableStateOf(doc) }
//        private set
//    init {
//        document = mutableStateOf(doc)
//    }
}