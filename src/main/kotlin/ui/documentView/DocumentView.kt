package ui.documentView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import data.DocumentViewControlState
import ui.PPCCanvas
import ui.PPCWindowState
import ui.documentView.sidebar.SideBar
import ui.documentView.toolbar.ToolDialog
import ui.documentView.toolbar.Toolbar
import ui.getLocalDrawingOffset


val DocumentView: @Composable (PPCWindowState, DocumentViewControlState) -> Unit =
    @Composable { windowState, windowControlState ->
        DocumentView(windowState, windowControlState)
    }

@Composable
fun DocumentView(windowState: PPCWindowState, documentViewControlState: DocumentViewControlState) {
    val documentController = documentViewControlState.documentController.value
    remember { documentViewControlState }
    Box(modifier = Modifier.background(Color(0xFFF8FCFF))) {
            PPCCanvas(documentController)
            Toolbar(documentViewControlState)

        if (documentViewControlState.sideBarActivated.value)
            SideBar(documentViewControlState, windowState)

        if (documentController.selection.value != null && documentController.selection.value!!.end.value != documentController.selection.value!!.start) {
            val localOffset = getLocalDrawingOffset(documentController.selection.value!!.end.value!!, documentController.state.document.value)
            val test = IntOffset(
                localOffset.x.toInt(),
                localOffset.y.toInt()
            )
            SelectionMenu(test, documentController)
        }

        documentViewControlState.activeDialog.value?.let {
            val x = it.first
            ToolDialog(documentViewControlState, it.second) {
                x(
                    documentController
                )
            }
        }
    }
}


