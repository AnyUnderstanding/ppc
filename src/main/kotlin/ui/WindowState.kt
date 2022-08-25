package ui

import ApplicationState
import androidx.compose.runtime.*
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import data.DocumentInformation

class PPCWindowState(
    val application: ApplicationState,
    private val exit: (PPCWindowState) -> Unit,
) {
    val window = WindowState(placement = WindowPlacement.Floating)

    suspend fun exit(): Boolean {
        exit(this)
        return true
    }

    fun newWindow() {
        application.newWindow()
    }


}