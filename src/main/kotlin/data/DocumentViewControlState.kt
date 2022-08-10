package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import control.DocumentController

class DocumentViewControlState(docCon: DocumentController) : WindowControlState {
    var activeDialog: MutableState<(@Composable (DocumentController) -> Unit)?> = mutableStateOf(null)
    val documentController = mutableStateOf(docCon)
    var loadedDoc = mutableStateOf("")
}