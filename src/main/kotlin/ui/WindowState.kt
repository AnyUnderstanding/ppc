package ui

import ApplicationState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowState

class PPCWindowState(
    private val application: ApplicationState,
    private val exit: (PPCWindowState) -> Unit,
) {
    val window = WindowState()
    val save: ((PPCWindowState) -> Unit)? = null

    suspend fun exit(): Boolean {
        exit(this)
        return true
    }

    suspend fun save(): Boolean {
        save?.let { it(this) }
        return true
    }


    fun newWindow() {
        application.newWindow()
    }
}