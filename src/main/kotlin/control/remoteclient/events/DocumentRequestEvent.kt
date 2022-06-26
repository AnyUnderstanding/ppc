package control.remoteclient.events

import control.DocumentController

// can't use private in constructor because Klaxon will break
class DocumentRequestEvent(type: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        val doc = documentController.state.document.value.toJSON()
        documentController.connectionController.send("{\"type\":\"docGet\", \"doc\":\"${doc.replace("\"","||")}\"}")
    }
}