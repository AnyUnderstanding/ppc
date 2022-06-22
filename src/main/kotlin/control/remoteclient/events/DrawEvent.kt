package control.remoteclient.events

import control.DocumentController
import util.Point

// can't use private in constructor because Klaxon will break
class DrawEvent(val editor: String, val x: Double, val y: Double) : Event("draw") {
    override suspend fun handle(documentController: DocumentController) {
        println("early in the morning")

        documentController.strokeEditedFromNetwork(Point(x,y), editor)
    }
}