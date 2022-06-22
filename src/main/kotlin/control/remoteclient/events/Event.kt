package control.remoteclient.events


import com.beust.klaxon.TypeFor
import control.DocumentController

// can't use private in constructor because Klaxon will break
@TypeFor(field = "type", adapter = EventTypeAdapter::class)
abstract class Event(val type: String) {

    abstract suspend fun handle(documentController: DocumentController)
}