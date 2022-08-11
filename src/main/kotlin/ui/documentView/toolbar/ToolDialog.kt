package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import control.DocumentController
import data.DocumentViewControlState

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ToolDialog(
    documentViewControlState: DocumentViewControlState,
    title: String,
    content: @Composable (DocumentController) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        Modifier.fillMaxSize().clickable(
            interactionSource = interactionSource,
            indication = null,
        ) {
            documentViewControlState.activeDialog.value = null

        },
    ) {

    }
    Card(
        modifier = Modifier.background(Color(0xFFF0F0F0))
    ) {
        Column(Modifier.width(IntrinsicSize.Min)) {
        Row(Modifier.fillMaxWidth().background(Color(0xFFE9E9E9)).height(40.dp), verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = title,
                modifier = Modifier.padding(10.dp),
                fontSize = TextUnit(1f, TextUnitType.Em),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6B6B6B)
            )
        }
        content(documentViewControlState.documentController.value)
        }
    }
}


