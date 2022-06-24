package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import control.DocumentController
import data.DocumentViewControlState

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
        content(documentViewControlState.documentController.value)
    }
}


