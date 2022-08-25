package control

import ApplicationState
import data.Document
import kotlinx.coroutines.*
import java.nio.file.Path

class AutoSaveJob(val application: ApplicationState) :
    CoroutineScope { // implement CoroutineScope to create local scope
    private var job: Job = Job()
    override val coroutineContext
        get() = Dispatchers.Default + job

    private var _doc: Document? = null
    private var _path: Path? = null

    // this method will help to stop execution of a coroutine.
    // Call it to cancel coroutine and to break the while loop defined in the coroutine below
    fun cancel() {
        println("$_doc -0- $_path")
        if (_doc != null && _path != null) {
            application.saveDocument(_doc!!, _path!!)
            job.cancel()
        }
    }

    fun schedule(document: Document, path: Path) = launch { // launching the coroutine
        val delaySeconds = 10L
        _doc = document
        _path = path
        while (coroutineContext.isActive) {
            println("save")
            application.saveDocument(document, path)
            delay(delaySeconds * 1000)
        }
    }
}