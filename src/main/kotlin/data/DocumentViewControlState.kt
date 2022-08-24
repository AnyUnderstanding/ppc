package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import control.DocumentController

class LoadedDoc {
    var workbook: MutableState<DocumentInformation?> = mutableStateOf(null)
    var folder: MutableState<DocumentInformation?> = mutableStateOf(null)
    var file: MutableState<DocumentInformation?> = mutableStateOf(null)

    fun getDeepestParentFolder(): DocumentInformation? {
        if (workbook.value == null) {
            if (folder.value == null) {
                return null
            }
            return folder.value
        }
        return workbook.value
    }
}

class DocumentViewControlState(docCon: DocumentController) : WindowControlState {
    var activeDialog: MutableState<Pair<(@Composable (DocumentController) -> Unit), String>?> = mutableStateOf(null)
    val documentController = mutableStateOf(docCon)
    var loadedDoc: MutableState<LoadedDoc> = mutableStateOf(LoadedDoc())
    var sideBarActivated = mutableStateOf(false)
}

