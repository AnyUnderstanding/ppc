package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import data.DocumentViewControlState
import ui.PPCWindowState


private val RegisterShape = GenericShape { size, _ ->
    val cornerRadius = (size.width + size.height) / 5
    lineTo(size.width - cornerRadius, 0f)
    cubicTo(size.width, 0f, size.width, 0f, size.width, cornerRadius)
    lineTo(size.width, size.height - cornerRadius)
    cubicTo(size.width, size.height, size.width, size.height, size.width - cornerRadius, size.height)
    lineTo(0f, size.height)
}

@Composable
fun SideBar(documentViewControlState: DocumentViewControlState, windowState: PPCWindowState) {
    Surface(modifier = Modifier.fillMaxHeight().background(Color.White)) {
        var activatedRegister by remember { mutableStateOf(-1) }

        val folders = windowState.application.loadDocumentInformation()
        // folder
        Row {
            Column {
                Row(Modifier.background(Color(0xFFF0F0F0)).fillMaxHeight(0.05f)) {

                }
                folders.forEachIndexed { i, it ->
                    (Register(
                        it.name,
                        Color.Red,
                        documentViewControlState.loadedDoc.value == it.name,
                        documentViewControlState
                    ))
                }
            }
            // documents
            if (folders.any { it.name == documentViewControlState.loadedDoc.value }) {
                Column {
                    Row(Modifier.background(Color(0xFFF0F0F0)).fillMaxHeight(0.05f)) {

                    }
                    folders.first { it.name == documentViewControlState.loadedDoc.value }.children.forEach {
                        (Register(
                            it.name,
                            Color.Transparent,
                            documentViewControlState.loadedDoc.value == it.name,
                            documentViewControlState
                        ))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Register(
    name: String,
    color: Color,
    activated: Boolean = false,
    documentViewControlState: DocumentViewControlState,
) {
    val height = 30.dp
    val lightColor = color.copy(alpha = 0.15f)
    Row(modifier = Modifier.padding(vertical = 10.dp).background(if (activated) lightColor else Color.Transparent)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = color)
        ) {

            documentViewControlState.loadedDoc.value = name
        }

    ) {

        Box(
            modifier = Modifier.size(20.dp, height).clip(RegisterShape).background(color)
        )
        Text(
            modifier = Modifier.height(height).padding(horizontal = 10.dp),
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(1.3f, TextUnitType.Em),
            textAlign = TextAlign.Center,
            color = Color(0xFF6B6B6B)
        )
    }
}

