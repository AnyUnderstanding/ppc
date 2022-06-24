package control.remoteclient.events

import control.DocumentController
import java.util.UUID

class EraseStrokeEvent(type: String, val strokeUUID: String) : Event(type) {

    override suspend fun handle(documentController: DocumentController) {
        documentController.strokeErasedFromNetwork(strokeUUID)
    }
}