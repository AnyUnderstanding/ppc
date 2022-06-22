package control.remoteclient.events

import control.DocumentController

// can't use private in constructor because Klaxon will break
class NewStrokeEvent(type: String, val editor: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        println("hey ho the drunken sailor")
        documentController.newStroke(true, editor)
    }
}