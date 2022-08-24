package ui

import ApplicationState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import data.Document
import data.DocumentInformation
import data.DocumentInformationType
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import java.io.File

class PPCWindowState(
    val application: ApplicationState,
    private val exit: (PPCWindowState) -> Unit,
) {
    val window = WindowState(placement = WindowPlacement.Maximized)
    val save: ((PPCWindowState) -> Unit)? = null

    var folders: MutableState<List<DocumentInformation>> = mutableStateOf(application.loadDocumentInformation())

    suspend fun exit(): Boolean {
        exit(this)
        return true
    }

    fun newWindow() {
        application.newWindow()
    }

    fun updateDocs() {
        folders.value = application.loadDocumentInformation()
    }
}