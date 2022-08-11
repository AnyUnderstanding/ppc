import androidx.compose.runtime.Composable
import data.Settings
import ui.PPCWindowState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import data.Document
import data.DocumentInformation
import data.DocumentInformationType
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import java.io.File

@Composable
fun rememberApplicationState() = remember {
    ApplicationState().apply {
        newWindow()
    }
}

class ApplicationState {
    val workingDirectoryPath = System.getProperty("user.home") + "/ppc/"

    val settings = Settings()

    private val _windows = mutableStateListOf<PPCWindowState>()
    val windows: List<PPCWindowState> get() = _windows

    fun newWindow() {
        // TODO: New Window
        _windows.add(
            PPCWindowState(
                application = this,
                exit = _windows::remove,
            )
        )
    }

    fun saveDocument(document: Document, name: String) {
        File(workingDirectoryPath + name).writeBytes(
            Cbor.encodeToByteArray(document)
        )
    }

    fun loadDocumentInformation(path: String = workingDirectoryPath, depth: Int = 3): List<DocumentInformation> {
        val folders = mutableListOf<DocumentInformation>()
        File(path).listFiles()?.forEach {
            if (it.isDirectory) {
                folders.add(
                    DocumentInformation(
                        it.name,
                        DocumentInformationType.Folder,
                        it.path
                    )
                )
            }
        }

        addChildrenToFolder(folders, depth - 1)

        return folders
    }

    fun addChildrenToFolder(folders: List<DocumentInformation>, remainingDepth: Int){
        folders.forEach {
            if (it.type == DocumentInformationType.Folder) {

                File(it.path).listFiles()?.forEach { file ->
                    if (file.isFile && file.name.endsWith(".ppc")) {
                        it.children.add(
                            DocumentInformation(
                                file.name.substringBefore('.'),
                                DocumentInformationType.Document,
                                file.path
                            )
                        )
                    } else if (file.isDirectory) {
                        it.children.add(DocumentInformation(file.name, DocumentInformationType.Folder, file.path))
                    }
                }
                if (remainingDepth > 0) addChildrenToFolder(it.children, remainingDepth - 1)
            }
        }
    }

    fun newFolder(name: String) {
        File(workingDirectoryPath + name).createNewFile()
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