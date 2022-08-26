package data

import androidx.compose.runtime.*
import control.DocumentController
import control.IDocumentController
import util.Point


class DocumentControlState(
    val documentController: IDocumentController,
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