package ui.documentView.toolbar.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import control.DocumentController
import ui.THEME
import util.ColorVector
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorSlider(DocumentController: DocumentController, callBack: (color: Color) -> Unit = {}) {

    var value by remember { mutableStateOf(0f) }
    var size = 15

    val rainbowColors = listOf(
        Color(0xFFFF0000),
        Color(0xFFFF9A00),
        Color(0xFFD0DE21),
        Color(0xFF3FDAD8),
        Color(0xFF2FC9E2),
        Color(0xFF1C7FEE),
        Color(0xFF5F15F2),
        Color(0xFFBA0CF8),
        Color(0xFFFB07D9),
        Color(0xFFFF0000),
    )
    var rainbowBrush = Brush.linearGradient(
        colors = rainbowColors
    )
    Box(
        modifier = Modifier.height(200.dp).width(300.dp),
        contentAlignment = Alignment.Center
    ) {
        var selectorOffsetX by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(1) }
        Box(
            modifier = Modifier.clip(RoundedCornerShape(size.dp)).fillMaxWidth(0.5f).background(rainbowBrush)
                .height(size.dp).onGloballyPositioned {
                    width = (it.size.width - it.size.height) // height is equal the diameter of the circle
                }
                .onPointerEvent(PointerEventType.Press){
                    selectorOffsetX = it.changes.first().position.x - size/2
                }
        ) {
            Box(
                Modifier
                    .offset { IntOffset(selectorOffsetX.roundToInt(), 0) }
                    .size(size.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            selectorOffsetX += dragAmount.x

                        }
                    }
            ) {
                Donut(getColorAt(value, rainbowColors), size.toFloat(), size.toFloat() / 8)
            }
        }
        if (selectorOffsetX > width) selectorOffsetX = width.toFloat()
        else if (selectorOffsetX < 0f) selectorOffsetX = 0f
        value = (0.9f * selectorOffsetX) / width



    }

    DocumentController.setColor(getColorAt(value, rainbowColors))
    callBack(getColorAt(value, rainbowColors))


}




fun getColorAt(value: Float, colors: List<Color>): Color {
    val i = (floor((value / 0.1))).toInt()
    val r = (value % 0.1) * 10

    if (r == 0.0) {
        return colors[i]
    }
    return ((colors[i + 1].toColorVector() - colors[i].toColorVector()) * r.toFloat() + colors[i].toColorVector()).toColor()

}

@Composable
fun Donut(color: Color, r: Float, width: Float) {


    val p1 = circlePath(r)
    val p2 = circlePath(r - width,width, width)
    Surface(modifier = Modifier.size(r.dp),
        color = THEME.value.menuBody,
        shape = object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                val p3 = Path()
                p3.op(p1, p2, PathOperation.Difference)
                return Outline.Generic(p3)
            }

        }
    ) {
    }

    Surface(modifier = Modifier.size(r.dp),
        color = color,
        shape = object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                return Outline.Generic(p2)
            }

        }
    ) {
    }
}




fun circlePath(r: Float, x: Float = 0f, y: Float = 0f): Path {
    return Path().apply {
        addOval(Rect(x, y, r, r))
    }
}
fun Color.toColorVector() = ColorVector(alpha, red, green, blue)