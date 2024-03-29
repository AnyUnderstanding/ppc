package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import data.DocumentViewControlState
import ui.THEME


private val tabs = listOf(Tab1, Tab2)

@Composable
fun Toolbar(documentViewControlState: DocumentViewControlState) {
    val tabIndex = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth().background(THEME.value.mainColor)) {
        ToolbarTabSelect(tabIndex, documentViewControlState)
        tabs[tabIndex.value](documentViewControlState)
    }
}
