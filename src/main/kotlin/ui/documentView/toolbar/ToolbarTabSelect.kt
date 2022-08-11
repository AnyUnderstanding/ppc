package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.DocumentViewControlState

@Composable
fun ToolbarTabSelect(tabIndex: MutableState<Int>, documentViewControlState: DocumentViewControlState) {
    // todo: make responsive
    Row(
        modifier = Modifier.fillMaxWidth().height(70.dp).background(color = Color(0xFFE9E9E9)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(false, "menu.svg", 34.dp) {  documentViewControlState.sideBarActivated.value = true }
        BarDivider()
        IconButton(tabIndex.value == 0, "pen.svg") { tabIndex.value = 0 }
        IconButton(tabIndex.value == 1, "add.svg") { tabIndex.value = 1 }
    }
}

