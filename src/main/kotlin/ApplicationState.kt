import androidx.compose.runtime.Composable
import data.Settings
import ui.PPCWindowState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import ui.documentView.DocumentView

@Composable
fun rememberApplicationState() = remember {
    ApplicationState().apply {
        newWindow()
    }
}

class ApplicationState {
    val settings = Settings();

    private val _windows = mutableStateListOf<PPCWindowState>()
    val windows: List<PPCWindowState> get() = _windows

    fun newWindow() {
        // TODO: New Window
        _windows.add(
            PPCWindowState(
                application = this,
                exit = _windows::remove,
            )
        )
    }

    suspend fun exit() {
        val windowsCopy = windows.reversed()
        for (window in windowsCopy) {
            if (!window.exit()) {
                break
            }
        }
    }

}