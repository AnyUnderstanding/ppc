package control

import androidx.compose.ui.geometry.Offset
import data.Tool
import util.Point

class MouseInputHandler(val documentController: DocumentController) : InputHandler {
    private var pressed: Boolean = false
    private var mousePos: Point = Point(0, 0)
    private var mouseDraged = false

    fun mousePressed(mousePos: Offset) {
        pressed = true
        documentController.toolMoved(Point(mousePos.x.toDouble(), mousePos.y.toDouble()))
    }


    fun mouseReleased() {
        pressed = false
        if (!mouseDraged) {
            documentController.toolClicked()
        }
        if (documentController.state.document.value.selectedTool.value == Tool.Pen) {
            if (documentController.selectedPage != null) {
                documentController.newStroke()
            }
        }
    }



    fun mouseMoved(mousePos: Offset) {
        this.mousePos = Point(mousePos.x.toDouble(), mousePos.y.toDouble())
        mouseDraged = false
        if (pressed) {
            documentController.toolMoved(Point(mousePos.x.toDouble(), mousePos.y.toDouble()))
            mouseDraged = true

        }
    }

    fun mouseWheelScroll(scrollDelta: Float) {
        if (!pressed)
            documentController.scrollY(scrollDelta * (30))
    }

    fun mouseWheelZoom(scrollDelta: Float) {
//        if (pressed)
        documentController.zoom(zoomDelta = scrollDelta * 0.5f, localMousePos = mousePos)
    }

}

