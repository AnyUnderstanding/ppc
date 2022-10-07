package control

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import data.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import util.Point

abstract class DocumentControllerBase(document: Document) : Controller {
    val mouse = MouseInputHandler(this)
    val state = mutableStateOf(DocumentControlState(this, document))

    val strokeController = StrokeController()
    private var canvasSize = Point(0, 0)
    var selection: MutableState<Selection?> = mutableStateOf(null)


    var localCenter = Point(0, 0)
        private set

    var selectedTool: MutableState<Tool> = mutableStateOf(Eraser())
    var selectedColor: Color = Color.Red

    val actionQueue = ActionQueue()

    fun toolDraggedEnded() {
        when (selectedTool.value) {
            is Selector -> {
                if (selection.value != null && !selection.value!!.isEmpty())
                    selection.value?.selectionComplete?.value = true
                else
                    selection.value = null
            }

            is TPen -> strokeController.endStroke()

            else -> {}

        }
    }

    fun toolDragged(point: Point) {

        val globalPoint = localCoordsToGlobal(point)

        when (selectedTool.value) {
            is TPen -> strokeAddPoint(globalPoint)
            is Eraser -> eraserMoved(globalPoint)
            is Selector -> selectorMoved(globalPoint)
            else -> {}
        }

    }

    fun toolDown(mousePos: Point) {
        when (selectedTool.value) {
            is Selector -> {
//                if (selection.value?.selectionComplete?.value!!) // NOTE: crashes application when selection starts over the toolbar canvas
                if (selection.value?.selectionComplete?.value == true)
                    selection.value = null
            }

            is TPen -> penDown(mousePos)

            else -> {}
        }
    }

    fun toolClicked() {

    }

    abstract fun penDown(mousePos: Point)

    abstract fun selectorMoved(globalPoint: Point)


    abstract fun strokeAddPoint(globalPoint: Point)


    abstract fun eraserMoved(globalPoint: Point)

    abstract fun newStroke(start: Point)


    fun resize(newXDim: Int, newYDim: Int, localCenter: Point) {
        canvasSize = Point(newXDim, newYDim)
        this.localCenter = localCenter
    }

    fun scrollY(scrollDelta: Float) {
        if (state.value.document.value.scrollY + scrollDelta < 0) return
        state.value.document.value.centerPoint.value += Point(0, scrollDelta.toDouble())
        state.value.document.value.scrollY += scrollDelta
    }

    fun scrollX(scrollDelta: Float) {
        state.value.document.value.centerPoint.value += Point(scrollDelta.toDouble(), 0)
        state.value.document.value.scrollX += scrollDelta
    }


    fun zoom(zoomDelta: Float, localMousePos: Point) {
        if (state.value.document.value.zoomFactor + zoomDelta <= 0) return

        val zoomFactor = 1 / (state.value.document.value.zoomFactor + zoomDelta)
        val newPos = localMousePos * zoomFactor
        val delta = localMousePos * (1 / state.value.document.value.zoomFactor) - newPos
        val newCenterPoint = state.value.document.value.centerPoint.value + delta
        state.value.document.value.updateZoom(zoomDelta, newCenterPoint)
    }

    fun setColor(color: Color) {
        selectedColor = color
        strokeController.stroke?.color = color
    }

    private fun localCoordsToGlobal(localPoint: Point): Point {
        val zoomFactor = 1 / state.value.document.value.zoomFactor
        return localPoint * zoomFactor + state.value.document.value.centerPoint.value - localCenter
    }

    abstract fun deleteSelection()

    fun moveSelection() {

        // todo ... fix (:
        selection.value?.selectedStrokes?.forEach {
            it.move(Point(30.0, 30.0))
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun docToBytes(): ByteArray = Cbor.encodeToByteArray(state.value.document.value)

    @OptIn(ExperimentalSerializationApi::class)
    fun loadDocFromBytes(bytes: ByteArray) {
        try {
            state.value.document.value = Cbor.decodeFromByteArray(bytes)
        } catch (e: Exception) {
            println("Could not open file: $e")
        }
    }

    abstract fun onRender()

    abstract fun redo()
    abstract fun undo()
}