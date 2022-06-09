package control

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import data.*
import util.Point
import kotlin.math.abs

class DocumentController : Controller {
    val mouse = MouseInputHandler(this)
    val state: DocumentControlState = DocumentControlState(this, Document(PageSize.A4))
    val strokeController = StrokeController()
    private var canvasSize = Point(0, 0)
    var selection: MutableState<Selection?> = mutableStateOf(null)
    var selectedPage: Page? = null
        private set

//    private var List<List<Point>>



    fun toolMoved(point: Point) {

        val globalPoint = localCoordsToGlobal(point)

        when (state.document.value.selectedTool.value) {
            Tool.Pen -> strokeAddPoint(globalPoint)
            Tool.Eraser -> eraserMoved(globalPoint)
            Tool.Selector -> selectorMoved(globalPoint)
        }

    }

    fun toolClicked() {
        when (state.document.value.selectedTool.value) {
            Tool.Selector -> {
                selection.value = null
            }
        }

    }

    private fun selectorMoved(globalPoint: Point) {

        getPageByPoint(globalPoint).let {
            if (it != selectedPage) {
                selectedPage = it

            }
        }
        if (selectedPage != null) {

            if (selection.value == null) {
                selection.value = Selection(globalPoint)
            }
            selection.value?.end?.value = globalPoint

            selection.value!!.selectedStrokes.clear()
            selection.value!!.selectedStrokes.addAll(
                selectedPage!!.strokes.filter { BoundingBox(selection.value!!.start, selection.value!!.end.value!!) in it.mainBoundingBox }
            )
        }

    }

    private fun strokeAddPoint(globalPoint: Point) {
        getPageByPoint(globalPoint).let {
            if (it != selectedPage) {
                selectedPage = it

                newStroke()
            }
        }
        if (selectedPage != null) {
            strokeController.addPoint(globalPoint)
        }
    }


    private fun eraserMoved(globalPoint: Point) {

        getPageByPoint(globalPoint).let {
            if (it != selectedPage) {
                selectedPage = it
            }

            // needed to avoid java.util.ConcurrentModificationException
            val erasedStrokes = mutableListOf<Stroke>()

            val eraserBoundingBox = BoundingBox(globalPoint - Point(5,5),globalPoint + Point(5,5))

                selectedPage?.strokes?.forEach { s ->
                    if (eraserBoundingBox !in s.mainBoundingBox) return@forEach

                    run bb@{
                        s.boundingBoxes.forEach { bb ->
                            if (eraserBoundingBox in bb) {

                                if ((abs((globalPoint - bb.point0).cross(bb.point1 - bb.point0))) / (bb.point1 - bb.point0).length < 10f * state.document.value.zoomFactor) {
                                    println((abs((globalPoint - bb.point0).cross(bb.point1 - bb.point0))) / (bb.point1 - bb.point0).length)
                                    erasedStrokes.add(s)
                                    return@bb
                                }

                            }
                        }
                    }
                }

            selectedPage?.strokes?.removeAll(erasedStrokes)

        }


    }

    fun newStroke() {
        val stroke = selectedPage?.newStroke(state.document.value.selectedColor)
        if (stroke != null)
            strokeController.newStroke(stroke)

    }

//    fun newStroke() {
//        val stroke = Stroke()
//        //state.strokes.add(stroke)
//       // strokeController.newStroke(stroke)
//
//    }

    fun getPageByPoint(point: Point): Page? {

        return try {
            state.document.value.pages.first {
                point.x in it.topLeft.x..(it.topLeft.x + state.document.value.pageSize.width) &&
                        point.y in it.topLeft.y..(it.topLeft.y + state.document.value.pageSize.height)
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun resize(newXDim: Int, newYDim: Int) {
        canvasSize = Point(newXDim, newYDim)
        println("resize  X: $newXDim  Y: $newYDim")
    }

    fun scrollY(scrollDelta: Float) {
        if (state.document.value.scrollY + scrollDelta < 0) return
        state.document.value.centerPoint.value += Point(0, scrollDelta.toDouble())
        state.document.value.scrollY += scrollDelta
        println(state.document.value.centerPoint.value)

    }

    fun newPage() {
        val document = state.document.value
        val tp = Point(10, (10 + (document.pageSize.height + 10) * document.pageCount).toDouble())
        document.newPage(PageType.Ruled, tp)
    }

    fun zoom(zoomDelta: Float, localMousePos: Point) {
        if (state.document.value.zoomFactor + zoomDelta <= 0) return

        val zoomFactor = 1 / (state.document.value.zoomFactor + zoomDelta)
        val newPos = localMousePos * zoomFactor
        val delta = localMousePos * (1 / state.document.value.zoomFactor) - newPos
        val newCenterPoint = state.document.value.centerPoint.value + delta
        state.document.value.setZoom(zoomDelta, newCenterPoint)
    }

    fun setColor(color: ULong) {
        state.document.value.selectedColor = color
        strokeController.stroke.color = color
    }

    private fun localCoordsToGlobal(localPoint: Point): Point {
        val zoomFactor = 1 / state.document.value.zoomFactor

        return localPoint * zoomFactor + state.document.value.centerPoint.value
    }

    fun deleteSelection() {
        selection.value?.selectedStrokes?.let { selectedPage?.strokes?.removeAll(it) }
    }

    fun moveSelection(){

        // todo ... fix (:
        selection.value?.selectedStrokes?.forEach{
            it.move(Point(30.0,30.0))
        }
    }


}