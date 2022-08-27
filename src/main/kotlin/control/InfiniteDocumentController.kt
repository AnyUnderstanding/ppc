package control


import data.BoundingBox
import data.Chunk
import data.Document
import data.Selection
import util.Point

class InfiniteDocumentController(document: Document) : DocumentControllerBase(document) {

    val chunks: MutableMap<Int, MutableMap<Int, Chunk>> = mutableMapOf()
    val chunkSize = Point(100, 100)

    var selectedChunk: Chunk? = null


    fun getChunkByPoint(point: Point, createIfNull: Boolean = false): Chunk? {
        val xChunk = (point.x / chunkSize.x).toInt()
        val yChunk = (point.y / chunkSize.y).toInt()
        var chunk = chunks[xChunk]?.get(yChunk)
        if (createIfNull) {
            if (chunk == null) {
                if (chunks[xChunk] == null)
                    chunks[xChunk] = mutableMapOf()

                chunk = Chunk()

                chunks[xChunk]!![yChunk] = chunk
            }
        }
        return chunk
    }

    override fun newStroke(start: Point) {
        var chunk = getChunkByPoint(start, createIfNull = true)!!

    }


    override fun penDown(mousePos: Point) {
        newStroke(mousePos)
    }

    override fun selectorMoved(globalPoint: Point) {
        getChunkByPoint(globalPoint, createIfNull = true)!!.let {
            if (it != selectedChunk) {
                selectedChunk = it
            }
        }

            if (selection.value == null) {
                selection.value = Selection(globalPoint)
            }
            selection.value?.end?.value = globalPoint

            selection.value!!.selectedStrokes.clear()
            selection.value!!.addStroke(
                selectedChunk!!.strokes.filter {
                    BoundingBox(
                        selection.value!!.start,
                        selection.value!!.end.value!!
                    ) in it.mainBoundingBox
                }
            )

    }

    override fun strokeAddPoint(globalPoint: Point) {
        getChunkByPoint(globalPoint, createIfNull = true)!!.let {
            if (it != selectedChunk) {
                selectedChunk = it

                newStroke(globalPoint)
            }
        }
            strokeController.addPoint(globalPoint)
    }

    override fun eraserMoved(globalPoint: Point) {
        TODO("Not yet implemented")
    }


    override fun deleteSelection() {
        TODO("Not yet implemented")
    }

    override fun onRender() {
        TODO("Not yet implemented")
    }

    override fun redo() {
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }
}