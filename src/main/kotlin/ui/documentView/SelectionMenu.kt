package ui.documentView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import control.DocumentController
import ui.THEME
import ui.documentView.toolbar.IconButton

@Composable
fun SelectionMenu(offset: IntOffset, documentController: DocumentController){
    Row (Modifier.offset{ offset }.clip(RoundedCornerShape(50)).background(THEME.value.mainColor)){
        SelectionMenuItem(icon = "delete.svg"){
            documentController.deleteSelection()
        }
        SelectionMenuItem(icon = "pen.svg"){
            documentController.moveSelection()
        }
    }
}
@Composable
fun SelectionMenuItem(icon: String, onClick: () -> Unit) {
    IconButton(false,icon, 30.dp,Modifier.height(40.dp), onClick)
}
