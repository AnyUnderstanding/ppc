package control.remoteclient.events

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import control.DocumentController
import data.Document
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import x

// can't use private in constructor because Klaxon will break
class DocumentLoadEvent(type: String, val doc: String) : Event(type) {
    override suspend fun handle(documentController: DocumentController) {
        try {
            val document = Json.decodeFromString<Document>(doc.replace("||", "\""))
            println(document.pages.size)
            println(document.pages[0].strokes.size)
            x = document

            documentController.state.document = mutableStateOf(document)
            println("loaded doc")


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}