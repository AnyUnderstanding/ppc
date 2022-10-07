package control

import ApplicationState
import data.Document
import kotlinx.coroutines.*
import java.nio.file.Path

class AutoSaveJob(private val application: ApplicationState) : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext
        get() = Dispatchers.Default + job

    var _doc: Document? = null
    var _path: Path? = null

    fun cancel() {
        if (_doc != null && _path != null) {
            application.saveDocument(_doc!!, _path!!)
            job.cancel()
        }
    }

    fun schedule(document: Document, path: Path) = launch { // launching the coroutine
        _doc = document
        _path = path
        while (coroutineContext.isActive) {
            application.saveDocument(document, path)
            delay(application.settings.saveDelay.toLong() * 1000)
        }
    }

    fun save() {
        if (_doc != null && _path != null) {
            application.saveDocument(_doc!!, _path!!)
        }
    }
}