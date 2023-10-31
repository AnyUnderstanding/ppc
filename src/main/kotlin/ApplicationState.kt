import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import data.*
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import ui.*
import java.nio.file.Path
import kotlin.io.path.*

@Composable
fun rememberApplicationState() = remember {
    ApplicationState().apply {
        newWindow()
    }
}

class ApplicationState {
    val settings = Settings()
    val pens = mutableStateListOf<Pen>(Pen(Color(0xA00000FF), 2f), Pen(Color.Blue, 1f))

    init {
        THEME.value = settings.theme
    }

    private val _windows = mutableStateListOf<PPCWindowState>()
    val windows: List<PPCWindowState> get() = _windows
    private val workingDirectoryPath = settings.ppcHome

    fun saveDocument(document: Document, path: Path) {
        path.writeBytes(
            Cbor.encodeToByteArray(document)
        )
    }

//    fun loadDocument(docInfo: DocumentInformation): Document? {
//        return loadDocument(docInfo.path)
//    }


    fun loadDocument(docPath: Path): Document? {
        runCatching {
            return Cbor.decodeFromByteArray<Document>(docPath.readBytes())
        }
        return null
    }


    fun loadDocumentInformation(
        path: Path = workingDirectoryPath,
        prefix: String = "",
        loadedDoc: LoadedDoc? = null
    ): DirectoryInformation {
        val document = DirectoryInformation(path.name, path, prefix)

        path.listDirectoryEntries().forEach {
            if (it.isDirectory()) {
                document.directories.add(loadDocumentInformation(it, "$prefix/${it.name}"))
            } else if (it.extension == "ppc") {
                document.files.add(
                    FileInformation(
                        it.nameWithoutExtension,
                        it,
                        "$prefix/${it.name}",
                        document, // backwards reference
                        it.fileSize()
                    )
                )
            }
        }

        if (loadedDoc != null) updateLoadedDoc(document, loadedDoc)

        return document
    }

    // TODO: Rework - cache latest opened
    private fun updateLoadedDoc(document: DirectoryInformation, loadedDoc: LoadedDoc) {
        loadedDoc.parent.value = document
//        loadedDoc.workbook.value = document.directories.firstOrNull()
//        loadedDoc.workbook.value = folders.firstOrNull {
//            it.path == loadedDoc.workbook.value?.path
//        }
//        loadedDoc.folder.value = loadedDoc.workbook.value?.children?.firstOrNull {
//            it.path == loadedDoc.folder.value?.path
//        }
    }

    fun newFolder(path: Path): Boolean {
        runCatching {
            path.createDirectories()
            return true
        }
        return false
    }

    fun newFile(doc: FileInformation?, name: String) {
        val parentPath = doc?.path ?: workingDirectoryPath
        runCatching {
            Path(parentPath, "$name.ppc").createFile()
        }
    }

    fun newWindow() {
        // TODO: New Window
        _windows.add(
            PPCWindowState(
                application = this,
                exit = _windows::remove
            )
        )
    }

    suspend fun exit() {
        val windowsCopy = windows.reversed()
        for (window in windowsCopy) {
            if (!window.exit()) {
                break
            }
        }
    }

}


fun Path(path: Path, varargs: String): Path = Path(path.pathString, varargs)