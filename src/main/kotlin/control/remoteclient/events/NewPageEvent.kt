package control.remoteclient.events

import control.DocumentController

class NewPageEvent(type: String, val pageUUID: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        documentController.newPage(fromNetwork = true, pageUUID)
    }
}