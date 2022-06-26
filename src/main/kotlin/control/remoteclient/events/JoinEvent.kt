package control.remoteclient.events

import control.DocumentController
import control.remoteclient.ConnectionController

// can't use private in constructor because Klaxon will break
class JoinEvent(type: String, val status: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        when(status){
            "success" -> {
                println("connected")
                println("fetching doc")

                documentController.state.connected.value = true
                documentController.connectionController.documentRequest()
            }
            "session not found" -> { println("unknown session") }
            else -> {}
        }
    }
}