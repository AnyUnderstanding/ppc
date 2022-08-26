package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import control.DocumentController
import data.*
import util.Point
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.input.pointer.*
import kotlin.math.*


var localCenter = Point(0, 0)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PPCCanvas(controller: DocumentController) {
    val state = controller.state.value
    val document = state.document.value
    var mousePos by remember { mutableStateOf(Offset(0f, 0f)) }

    var isRendered = false;


    Canvas(
        modifier = Modifier.fillMaxSize().onPointerEvent(PointerEventType.Move) {
            //controller.mouse.mouseMoved(it.changes.first().position)
            controller.mouse.inputMoved(it.changes.first().position)
            mousePos = it.changes.first().position
        }.onPointerEvent(PointerEventType.Press) {
            //controller.mouse.mousePressed(it.changes.first().position)
            controller.mouse.inputDown(it.changes.first().position)
        }.onPointerEvent(PointerEventType.Release) {
            //controller.mouse.mouseReleased()
            controller.mouse.inputUp(it.changes.first().position)

        }.onSizeChanged {
//            val nc = Point(it.width/2, it.height * 0.2)
//            if (nc == localCenter) return@onSizeChanged
            localCenter = Point(it.width / 2, it.height * 0.2)
            controller.resize(it.width, it.height, localCenter)
        }.onPointerEvent(PointerEventType.Scroll) {
            if (it.keyboardModifiers.isCtrlPressed)
                controller.mouse.mouseWheelZoom(-it.changes.first().scrollDelta.y / 2)
            else
                controller.mouse.mouseWheelScroll(it.changes.first().scrollDelta)
        }.onPointerEvent(PointerEventType.Move) {
            it.changes.first()
        }
    ) {
        if (!isRendered) {
            isRendered = true
            controller.onRender()
        }
        /*
        drawPage(
            Page(pageType = PageType.Blanc, Point(0, 0)),
            getLocalDrawingOffset(Point(1.0, 1.0), document),
            Point(PageSize.A4.width.toDouble(), PageSize.A4.height.toDouble()) * document.zoomFactor * 3
        )*/

//        document.value.scrollY / document.value.pageSize.ySize
//        document.value
        document.pages.forEachIndexed { i, p ->

            /*
       (x,y)---------
            |       |
            |       |
            |       |
            |       |
            ---------(nx, ny)
            Seiten Anfang: i * pHeight
            Seiten Ende: (i + 1) * pHeight


            Obere Seite:
            i * pHeight <= scroll && (i+1) * pHeight >= scroll
            Mittige Seiten:
            i * pHeight >= scroll && (i+1) * pHeight <= scroll + wHeight
            Untere Seite:
            i * pHeight >= scroll && i * pHeight <= scroll + wHeight
             */
//            if (
//                i * height <= scroll && (i + 1) * height >= scroll ||
//                i * height >= scroll && (i + 1) * height <= scroll + h ||
//                i * height >= scroll && i * height <= scroll + h
//            )
            drawPage(
                p,
                getLocalDrawingOffset(p.topLeft, document),
                Point(document.pageSize.width.toDouble(), document.pageSize.height.toDouble()) * document.zoomFactor,
                document
            )
        }


        document.pages.forEach {
            it.strokes.forEach strokeLoop@{ s ->

                val path = Path()
                if (s.spline.size > 0)
                    path.moveTo(getLocalDrawingOffset(s.spline[0], document))
                else if (s.pendingPoints.size > 0)
                    path.moveTo(getLocalDrawingOffset(s.pendingPoints[0], document))
                else return@strokeLoop
                s.spline.drop(1).forEachIndexed { i, p ->
                    // println("from:  ${s.spline[i]}  -  to: $p")


                    if (s.spline[i].x.isNaN() || s.spline[i].y.isNaN() || p.x.isNaN() || p.y.isNaN()) return@forEachIndexed

//                    drawLine(
//                        color = Color.Red,
//                        start = getLocalDrawingOffset(s.spline[i], document = document),
//                        end = getLocalDrawingOffset(p, document = document),
//                        3.0F * document.zoomFactor
//                    )
                    path.lineTo(getLocalDrawingOffset(p, document))
                    // drawCircle(color = Color.Blue, center = p.toOffset(), radius = 2.0F)
                }

                s.pendingPoints.forEach { p ->
                    path.lineTo(getLocalDrawingOffset(p, document))
                }










                drawPath(
                    path = path,
                    color = s.color,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = s.width * document.zoomFactor,
                        cap = StrokeCap.Round
                    )
                )


            }
        }

        when (state.documentController.selectedTool.value) {
            is Eraser -> drawCircle(
                color = Color(0xAA9C9C9C),
                center = mousePos,
                radius = 10.0F
            )

            is TPen -> drawCircle(
                color = Color(0xFF9C9C9C),
                center = mousePos,
                radius = 3.0F
            )

            is Selector -> {
                state.documentController.selection.value?.let { selection ->
                    if (selection.end.value == null) return@let
                    val selectionPath = Path()
                    selectionPath.moveTo(getLocalDrawingOffset(selection.start, document))


                    val start = getLocalDrawingOffset(selection.start, document)
                    val end = getLocalDrawingOffset(selection.end.value!!, document)

                    selectionPath.addRect(
                        Rect(
                            Offset(min(start.x, end.x), min(start.y, end.y)),
                            Offset(max(start.x, end.x), max(start.y, end.y))
                        )
                    )

                    val strokeBoundingPath = Path()

                    selection.strokeBoundingBox?.let { bb ->

                        val sStart = getLocalDrawingOffset(bb.point0, document)
                        val sEnd = getLocalDrawingOffset(bb.point1, document)
                        strokeBoundingPath.addRect(
                            Rect(
                                Offset(min(sStart.x, sEnd.x) - 5, min(sStart.y, sEnd.y) - 5),
                                Offset(max(sStart.x, sEnd.x) + 5, max(sStart.y, sEnd.y) + 5)
                            )
                        )

                    }
                    drawPath(
                        path = strokeBoundingPath,
                        color = Color(0xAA7B7B7B),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 1.5F,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(
                                    10F * document.zoomFactor,
                                    10F * document.zoomFactor
                                ), 00F
                            )
                        )
                    )

                    drawPath(
                        path = selectionPath,
                        color = Color(0xAA7B7B7B),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 1.5F,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(
                                    10F  * document.zoomFactor,
                                    10F  * document.zoomFactor
                                ), 00F
                            )
                        )
                    )
                }
            }
        }
    }
}


fun DrawScope.drawPage(page: Page, topLeftPos: Offset, pageSize: Point, document: Document) {
    val path = Path()
    path.addRect(Rect(topLeftPos, Size(pageSize.x.toFloat(), pageSize.y.toFloat())))

    drawPath(
        path = path,
        color = Color.DarkGray,
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 1F,
            cap = StrokeCap.Round,
        )
    )
    drawRect(color = Color.White, topLeft = topLeftPos, size = Size(pageSize.x.toFloat(), pageSize.y.toFloat()))

//    drawLine(Color.Black, topLeftPos, topLeftPos + Offset(0, ))
    val p = Path()
    val gridSize = 30
    val horizontalDelta = (pageSize.y / document.pageSize.height) * gridSize
    var verticalDelta = (pageSize.x / document.pageSize.width) * gridSize
    var horizontalLines = (pageSize.y / horizontalDelta).roundToInt()
    var verticalLines = (pageSize.x / verticalDelta).roundToInt()

    var strokeStyle: DrawStyle = androidx.compose.ui.graphics.drawscope.Stroke()
    when (page.pageType) {
        PageType.Ruled -> {
            val lineDistance = (pageSize.y / 33).toFloat()

            p.moveTo(topLeftPos.x, topLeftPos.y + lineDistance * 2)
            for (i in 0..30) {
                p.lineTo((topLeftPos.x + pageSize.x).toFloat(), topLeftPos.y + lineDistance * 2 + lineDistance * i)
                p.moveTo(topLeftPos.x, topLeftPos.y + lineDistance * 2 + lineDistance * (i + 1))
            }

        }

        PageType.Checkered -> {


            for (i in 1 until horizontalLines) {
                p.moveTo(topLeftPos.x, (topLeftPos.y + horizontalDelta * i).toFloat())
                p.lineTo((topLeftPos.x + pageSize.x).toFloat(), (topLeftPos.y + horizontalDelta * i).toFloat())
            }

            for (i in 1 until verticalLines) {
                p.moveTo((topLeftPos.x + verticalDelta * i).toFloat(), topLeftPos.y)
                p.lineTo((topLeftPos.x + verticalDelta * i).toFloat(), (topLeftPos.y + pageSize.y).toFloat())
            }
        }

        PageType.Dotted -> {
            strokeStyle = androidx.compose.ui.graphics.drawscope.Fill

            val dotSize = 2 * document.zoomFactor
            for (hi in 1 until horizontalLines) {
                for (vi in 1 until verticalLines) {
                    p.moveTo(
                        (topLeftPos.x + verticalDelta * vi).toFloat(),
                        (topLeftPos.y + horizontalDelta * hi).toFloat()
                    )
                    p.addOval(Rect((topLeftPos.x + verticalDelta * vi).toFloat(),(topLeftPos.y + horizontalDelta * hi).toFloat(),(topLeftPos.x + verticalDelta * vi).toFloat() + dotSize,(topLeftPos.y + horizontalDelta * hi).toFloat() + dotSize))


                }
            }


        }

        PageType.Isometric -> {


            for (i in 1 until horizontalLines) {

                val posY1L = pageSize.x * (-30 * (PI / 180)) + topLeftPos.y + + horizontalDelta * i
                val posY2L = pageSize.x * (30 * (PI / 180)) + topLeftPos.y + + horizontalDelta * i

                p.moveTo(topLeftPos.x, (topLeftPos.y + horizontalDelta * i).toFloat())
                p.lineTo((topLeftPos.x+pageSize.x).toFloat(), posY1L.toFloat())

                p.moveTo(topLeftPos.x, (topLeftPos.y + horizontalDelta * i).toFloat())
                p.lineTo((topLeftPos.x+pageSize.x).toFloat(), posY2L.toFloat())

                val posY1R = pageSize.x * (30 * (PI / 180)) + topLeftPos.y + + horizontalDelta * i
                val posY2R = pageSize.x * (-30 * (PI / 180)) + topLeftPos.y + + horizontalDelta * i

                p.moveTo((topLeftPos.x+pageSize.x).toFloat(), (topLeftPos.y + horizontalDelta * i).toFloat())
                p.lineTo(topLeftPos.x, posY1R.toFloat())

                p.moveTo((topLeftPos.x+pageSize.x).toFloat(), (topLeftPos.y + horizontalDelta * i).toFloat())
                p.lineTo(topLeftPos.x, posY2R.toFloat())

            }
        }
        PageType.Blanc->{/* Nothing to do */}

        else -> {}


    }
    drawPath(path = p, color = Color.DarkGray, style = strokeStyle)

}


fun getLocalDrawingOffset(globalPoint: Point, document: Document): Offset {
    val zoomFactor = document.zoomFactor
    val delta = localCenter - document.centerPoint.value
    return ((globalPoint + delta) * zoomFactor).toOffset()
}


/*
    val zoomFactor = document.zoomFactor.value
    val zoomOffsetX = (document.centerPoint.x - (w * 1/zoomFactor * 0.5)) - document.scrollX.value
    val zoomOffsetY = (document.centerPoint.y - (h * 1/zoomFactor * 0.5)) - document.scrollY.value
    val globalX = globalOffset.x * zoomFactor + zoomOffsetX*zoomFactor + document.scrollX.value
    val globalY = globalOffset.y * zoomFactor + zoomOffsetY*zoomFactor + document.scrollY.value

    // return Offset(globalX.toFloat(),globalY.toFloat())
 */

fun Path.moveTo(offset: Offset) {
    this.moveTo(offset.x, offset.y)
}

fun Path.lineTo(offset: Offset) {
    this.lineTo(offset.x, offset.y)
}