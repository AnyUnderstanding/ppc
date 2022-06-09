package ui.documentView.toolbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ToolSelectButton(isActivated: Boolean, iconPath: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val defaultOffset = 15f
    val duration = 200
    val offsetY  =  remember { Animatable(defaultOffset) }
    val coroutineScope = rememberCoroutineScope()




    Column(
        modifier = Modifier.aspectRatio(1f).clickable(
            interactionSource = interactionSource,
            indication = null
        ) { onClick() }.clipToBounds()
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


            Image(
            painter = painterResource(iconPath),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.offset {
                IntOffset(
                    0,
                    offsetY.value.roundToInt()
                )
            }
        )
        if (isActivated) {
            coroutineScope.launch {
                offsetY.animateTo(
                    targetValue = 5f,
                    animationSpec = tween(
                        durationMillis = duration,
                        delayMillis = 0,

                    )
                )
            }

        }else{
            coroutineScope.launch {
                offsetY.animateTo(
                    targetValue = defaultOffset,
                    animationSpec = tween(
                        durationMillis = duration,
                        delayMillis = 0,

                    )
                )
            }
        }
    }
}