package control

import data.*
import util.Point
import kotlin.math.abs

class DocumentController(document: Document) : DocumentControllerBase(document) {

    var selectedPage: Page? = null
        private set


    override fun penDown(mousePos: Point) {
        if (selectedPage != null) newStroke()
    }

    override fun selectorMoved(globalPoint: Point) {

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
            selection.value!!.addStroke(
                selectedPage!!.strokes.filter {
                    BoundingBox(
                        selection.value!!.start,
                        selection.value!!.end.value!!
                    ) in it.mainBoundingBox
                }
            )
        }

    }

    override fun strokeAddPoint(globalPoint: Point) {
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


    override fun eraserMoved(globalPoint: Point) {

        getPageByPoint(globalPoint).let {
            if (it != selectedPage) {
                selectedPage = it
            }

            // needed to avoid java.util.ConcurrentModificationException
            val erasedStrokes = mutableListOf<Stroke>()

            val eraserBoundingBox = BoundingBox(globalPoint - Point(5, 5), globalPoint + Point(5, 5))

            selectedPage?.strokes?.forEach { s ->
                if (eraserBoundingBox !in s.mainBoundingBox) return@forEach

                run bb@{
                    s.boundingBoxes.forEach { bb ->
                        if (eraserBoundingBox in bb) {

                            if ((abs((globalPoint - bb.point0).cross(bb.point1 - bb.point0))) / (bb.point1 - bb.point0).length < 10f * state.value.document.value.zoomFactor) {
                                println((abs((globalPoint - bb.point0).cross(bb.point1 - bb.point0))) / (bb.point1 - bb.point0).length)
                                erasedStrokes.add(s)
                                return@bb
                            }

                        }
                    }
                }
            }

            selectedPage?.removeStrokes(erasedStrokes)
            if (selectedPage != null && erasedStrokes.size > 0)
                actionQueue.addAction(DeleteAction(erasedStrokes, selectedPage!!))


        }


    }

    override fun newStroke(start: Point) {
        TODO("Not yet implemented")
    }

    fun newStroke() {
        val stroke = selectedPage?.addStroke((selectedTool.value as TPen).pen.color, (selectedTool.value as TPen).pen.width)
        if (stroke != null) {
            strokeController.newStroke(stroke)
            actionQueue.addAction(DrawAction(listOf(stroke), selectedPage!!))
        }

    }

//    fun newStroke() {
//        val stroke = Stroke()
//        //state.value.strokes.add(stroke)
//       // strokeController.newStroke(stroke)
//
//    }

    fun getPageByPoint(point: Point): Page? {

        return try {
            state.value.document.value.pages.first {
                point.x in it.topLeft.x..(it.topLeft.x + state.value.document.value.pageSize.width) &&
                        point.y in it.topLeft.y..(it.topLeft.y + state.value.document.value.pageSize.height)
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun newPage() {
        val document = state.value.document.value

        document.newPage(
            PageType.Checkered,
            Point(-document.pageSize.width / 2.0, ((document.pageSize.height + 10) * document.pageCount).toDouble())
        )
    }



    private fun localCoordsToGlobal(localPoint: Point): Point {
        val zoomFactor = 1 / state.value.document.value.zoomFactor
        return localPoint * zoomFactor + state.value.document.value.centerPoint.value - localCenter
    }

    override fun deleteSelection() {

        selection.value?.selectedStrokes?.let {
            if (selectedPage != null)
                actionQueue.addAction(DeleteAction(it, selectedPage!!))

            selectedPage?.removeStrokes(it)
        }
        selection.value = null
    }




    override fun onRender() {
        if (state.value.document.value.pages.size == 0)
            newPage()
    }

    override fun redo() {

        when(val action = actionQueue.redo()){
            is DrawAction -> {
                action.strokeHolder.addStrokes(action.strokes)
            }
            is DeleteAction -> {
                action.strokeHolder.removeStrokes(action.strokes)
            }
        }
    }

    override fun undo() {

        when(val action = actionQueue.undo()){
            is DrawAction -> {
                action.strokeHolder.removeStrokes(action.strokes)
            }
            is DeleteAction -> {
                action.strokeHolder.addStrokes(action.strokes)

            }
        }
    }


}