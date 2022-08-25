package control

import ApplicationState
import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import data.Document
import data.DocumentViewControlState
import data.PageSize
import ui.PPCWindow
import ui.documentView.DocumentView


@Composable
fun ApplicationScope.PPCApplication(state: ApplicationState) {

    for (window in state.windows) {
        key(window) {
            val documentViewControlState = remember { DocumentViewControlState(DocumentController(Document(PageSize.A4)), state) }

            PPCWindow(window, documentViewControlState, DocumentView)
        }
    }
}
