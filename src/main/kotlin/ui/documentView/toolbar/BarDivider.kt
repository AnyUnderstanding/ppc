package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.THEME

@Composable
fun BarDivider() {
    Box(
        modifier = Modifier.fillMaxHeight()
    ){
    Divider(
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .width(2.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(THEME.value.dividerColor)
            .align(Alignment.Center)

    )
    }
}