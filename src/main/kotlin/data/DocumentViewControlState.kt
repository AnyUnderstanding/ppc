package data

import ApplicationState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import control.AutoSaveJob
import control.DocumentController
import control.DocumentControllerBase
import java.nio.file.Path
import Path
import kotlin.io.path.exists
import kotlin.io.path.fileSize

class LoadedDoc(private val documentViewControlState: DocumentViewControlState, info: DirectoryInformation) {
    val parent = mutableStateOf(info)

    private var _file: MutableState<FileInformation?> = mutableStateOf(null)
    var file: FileInformation?
        get() = _file.value
        set(value) {
            _file.value = value
            if (value != null) {
                documentViewControlState.loadDocument(value.path)
            }
        }
}


class DocumentViewControlState(docCon: DocumentController, private val application: ApplicationState) :
    WindowControlState {
    var activeDialog: MutableState<Triple<(@Composable (DocumentController) -> Unit), String, () -> Unit>?> =
        mutableStateOf(null)
    var documentController = mutableStateOf(docCon)
    var sideBarActivated = mutableStateOf(false)
    var document: MutableState<DirectoryInformation> =
        mutableStateOf(application.loadDocumentInformation())
    var loadedDoc = mutableStateOf(LoadedDoc(this, document.value))
    var autoSaveJob = AutoSaveJob(application)

    fun loadDocument(docPath: Path) {
        application.loadDocument(docPath)?.let {
            autoSaveJob.cancel()
            autoSaveJob = AutoSaveJob(application)
            documentController.value = DocumentController(it)
            autoSaveJob.schedule(it, docPath)
        }
    }

    fun saveDocument() {
        autoSaveJob.save()
    }

    fun createDocument(docParent: DirectoryInformation, name: String, size: PageSize = PageSize.A4) {
        val doc = Document(size)
        val path = Path(docParent.path, "$name.ppc")
        if (path.exists()) return
        application.saveDocument(doc, path)
        loadDocument(path)
        docParent.files.add(FileInformation(name, path, parent = docParent, size = path.fileSize()))
    }

    fun createFolder(docParent: DirectoryInformation, name: String) {
        val path = Path(docParent.path, name)
        application.newFolder(path)
        docParent.directories.add(DirectoryInformation(name, path))
    }

    fun updateDocs() {
        document.value = application.loadDocumentInformation(loadedDoc = loadedDoc.value)
    }

    override fun finalize() {
        autoSaveJob.cancel()
    }

    fun addPen(pen: Pen) {
        application.pens.add(pen)
    }

    fun getPens(): MutableList<Pen> {
        return application.pens
    }
}

