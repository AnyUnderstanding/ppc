@file:OptIn(ExperimentalSerializationApi::class)
package ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.*
import control.Controller
import control.DocumentController
import data.Document
import data.WindowControlState
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.awt.FileDialog
import java.io.File
import java.nio.file.Path


@Composable
fun <T : WindowControlState> PPCWindow(
    windowState: PPCWindowState,
    controlState: T,
    RenderWindow: @Composable (PPCWindowState, T) -> Unit
) {
    val scope = rememberCoroutineScope()
    fun exit() = scope.launch { windowState.exit() }


    Window(
        state = windowState.window,
        title = "",
        onCloseRequest = { exit() }
    ) {

        RenderWindow(windowState, controlState)
    }
}

//@OptIn(DelicateCoroutinesApi::class)
private suspend fun File.saveDocument(document: Document) {
    val bytes = Cbor.encodeToByteArray(document)
    writeBytes(bytes)
}

private suspend fun File.openDocument(): Document {
    val bytes = readBytes()
    return Cbor.decodeFromByteArray(bytes)
}

@Composable
fun FrameWindowScope.FileDialog(
    title: String,
    isLoad: Boolean,
    onResult: (result: Path?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(window, "Choose a file", if (isLoad) LOAD else SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    if (file != null) {
                        onResult(File(directory).resolve(file).toPath())
                    } else {
                        onResult(null)
                    }
                }
            }
        }.apply {
            this.title = title
        }
    },
    dispose = FileDialog::dispose
)
