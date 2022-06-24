package control.remoteclient.events

import control.DocumentController
import data.Document
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// can't use private in constructor because Klaxon will break
class DocumentLoadEvent(type: String, val doc: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        try {
            val document = Json.decodeFromString<Document>(doc)
            documentController.state.document.value = document
        }catch (_: Exception){}
    }
}