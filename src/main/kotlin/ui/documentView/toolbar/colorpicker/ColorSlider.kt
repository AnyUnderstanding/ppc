package ui.documentView.toolbar.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import control.DocumentController
import util.ColorVector
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun ColorSlider(DocumentController: DocumentController) {

    var value by remember { mutableStateOf(0f) }
    var diameter = 15

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
        var offsetX by remember { mutableStateOf(0f) }
        var width by remember { mutableStateOf(0) }

        Box(
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).fillMaxWidth(0.5f).background(rainbowBrush)
                .height(diameter.dp).onGloballyPositioned {
                    width = (it.size.width - it.size.height) // height is equal the diameter of the circle
                }) {
            Box(
                Modifier
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .size(diameter.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            offsetX += dragAmount.x
                            if (offsetX > width) offsetX = width.toFloat()
                            else if (offsetX < 0f) offsetX = 0f

                            value = (0.9f * offsetX) / width
                        }
                    }
            ) {
                Donut()
            }
        }
    }

    DocumentController.setColor(getColorAt(value, rainbowColors))


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
fun Donut() {
    Surface(modifier = Modifier.size(20.dp),
        color = Color.White,
        shape = object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                val thickness = size.height / 8
                val p1 = Path().apply {
                    addOval(Rect(0f, 0f, size.width - 1, size.height - 1))
                }
                val p2 = Path().apply {
                    addOval(
                        Rect(
                            thickness,
                            thickness,
                            size.width - 1 - thickness,
                            size.height - 1 - thickness
                        )
                    )
                }
                val p3 = Path()
                p3.op(p1, p2, PathOperation.difference)
                return Outline.Generic(p3)
            }

        }
    ) {
    }
}

fun Color.toColorVector() = ColorVector(alpha, red, green, blue)