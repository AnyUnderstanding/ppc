package control

import ApplicationState
import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import data.DocumentViewControlState
import ui.PPCWindow
import ui.documentView.DocumentView


@Composable
fun ApplicationScope.PPCApplication(state: ApplicationState) {

    for (window in state.windows) {
        key(window) {
            PPCWindow(window, DocumentViewControlState(DocumentController()), DocumentView)
        }
    }
}