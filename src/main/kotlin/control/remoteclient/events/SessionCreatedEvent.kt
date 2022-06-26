package control.remoteclient.events

import control.DocumentController
import java.util.UUID

class SessionCreatedEvent(type: String, val status: String, val UUID: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        println("llll")
        when(status){
            "success" -> {
                println("connected")
                documentController.state.sessionID = UUID
                documentController.state.connected.value = true
            }
            "session not found" -> { println("unknown session") }
            else -> {}
        }

    }
}