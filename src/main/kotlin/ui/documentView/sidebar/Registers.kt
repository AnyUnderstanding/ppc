package ui.documentView.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import data.DirectoryInformation
import data.DocumentViewControlState
import data.LoadedDoc

@Composable
fun Registers(
    dirs: List<DirectoryInformation>,
    indent: Int,
    loaded: LoadedDoc,
    documentViewControlState: DocumentViewControlState
) {
    dirs.forEach {
        Register(it, indent, loaded, documentViewControlState)
    }
}