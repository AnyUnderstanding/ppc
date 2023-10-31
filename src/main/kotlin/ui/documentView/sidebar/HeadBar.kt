package ui.documentView.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DocumentViewControlState
import ui.THEME
import ui.documentView.toolbar.IconButton

@Composable
fun HeadBar(documentViewControlState: DocumentViewControlState) {
    Row(
        Modifier.background(THEME.value.secondaryColor).height(70.dp).fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        SearchBar()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(false, "newFile.svg")
            IconButton(false, "leftArrow.svg") { documentViewControlState.sideBarActivated.value = false }
        }
    }
}