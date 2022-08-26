package data

import ApplicationState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import control.AutoSaveJob
import control.DocumentController
import java.nio.file.Path
import kotlin.io.path.pathString

class LoadedDoc(val documentViewControlState: DocumentViewControlState) {
    var workbook: MutableState<DocumentInformation?> = mutableStateOf(null)
    var folder: MutableState<DocumentInformation?> = mutableStateOf(null)
    private var _file: MutableState<DocumentInformation?> = mutableStateOf(null)
    var file: DocumentInformation?
        get() = _file.value
        set(value) {
            _file.value = value
            if (value != null) {
                documentViewControlState.loadDocument(value)
            }
        }

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


class DocumentViewControlState(docCon: DocumentController, private val application: ApplicationState) : WindowControlState {


    var activeDialog: MutableState<Triple<(@Composable (DocumentController) -> Unit), String,  () -> Unit>?> = mutableStateOf(null)
    var documentController = mutableStateOf(docCon)
    var sideBarActivated = mutableStateOf(false)
    var loadedDoc = mutableStateOf(LoadedDoc(this))
    var folders: MutableState<List<DocumentInformation>> =
        mutableStateOf(application.loadDocumentInformation(loadedDoc = loadedDoc.value))
    var autoSaveJob = AutoSaveJob(application)



    fun loadDocument(docInfo: DocumentInformation) {
        loadDocument(Path.of(docInfo.path))
    }

    fun loadDocument(docPath: Path) {
        application.loadDocument(docPath)?.let {
            autoSaveJob.cancel()

            autoSaveJob = AutoSaveJob(application)

            documentController.value = DocumentController(it)
            autoSaveJob.schedule(it, docPath)
        }
    }

    fun createDocument(docParent: DocumentInformation, name: String) {
        val doc = Document(PageSize.A4)
        val path = Path.of(docParent.path, "$name.ppc")
        application.saveDocument(doc, path)
        loadDocument(path)
    }

    fun updateDocs() {
        folders.value = application.loadDocumentInformation(loadedDoc = loadedDoc.value)
    }

    override fun finalize() {
        autoSaveJob.cancel()
    }

    fun addPen(pen: Pen){
        application.pens.add(pen)
    }

    fun getPens(): MutableList<Pen>{
        return application.pens
    }
}

