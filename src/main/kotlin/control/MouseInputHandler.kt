package control

import androidx.compose.ui.geometry.Offset
import util.Point

class MouseInputHandler(val documentController: DocumentControllerBase) : InputHandler {
    private var pressed: Boolean = false
    private var mousePos: Point = Point(0, 0)
    private var mouseDraged = false

    fun mouseWheelScroll(delta: Offset) {
        val speed = 30
        if (!pressed) {
            documentController.scrollX(delta.x * speed)
            documentController.scrollY(delta.y * speed)
        }
    }

    fun mouseWheelZoom(scrollDelta: Float) {
//        if (pressed)
        documentController.zoom(zoomDelta = scrollDelta * 0.5f, localMousePos = mousePos)
    }


    private var isPressed = false
    private var mouseMoved = false
    override fun inputDown(position: Offset) {
        mouseMoved = false
        isPressed = true
        mousePos = Point(position.x.toDouble(), position.y.toDouble())
        documentController.toolDragged(mousePos)
        documentController.toolDown(mousePos)

    }

    override fun inputUp(position: Offset) {
        if (!mouseMoved) {
            documentController.toolClicked()
        }

        documentController.toolDraggedEnded()

        mouseMoved = false
        isPressed = false

    }

    override fun inputMoved(position: Offset) {
        mouseMoved = true
        mousePos = Point(position.x.toDouble(), position.y.toDouble())
        if (isPressed) {
            documentController.toolDragged(mousePos)
        }
    }




}

