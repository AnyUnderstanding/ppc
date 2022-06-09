package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// when to load images -> loading images with every recompose is probably bad
@Composable
fun IconButton(isActivated: Boolean, iconPath: String,size: Dp = 40.dp, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.then(modifier).aspectRatio(1f).clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        painterResource(iconPath)
        Icon(
            modifier = Modifier.size(size),
            painter = painterResource(iconPath),
            contentDescription = null,
            tint = Color(0xFF6B6B6B)
        )
        if (isActivated) {
            Box(
                contentAlignment = Alignment.Center
            )
            {

                Divider(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFF13C6FF))

                )
            }
        }
    }
}