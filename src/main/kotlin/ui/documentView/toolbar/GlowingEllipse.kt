package ui.documentView.toolbar

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun GlowingEllipse(){
    val color = 0xFF0000FF
    val step = 0xFF000000
    var fade = color
    Canvas(modifier = Modifier){

        drawOval(Color(color), size = Size(30f,30f))

        while (fade > (color - 0xFF000000)){
            println("loop")
            drawOval(Color(color), size = Size(30f,30f))
            fade -= step
        }
    }

}