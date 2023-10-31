package ui.documentView.sidebar

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.DirectoryInformation
import data.DocumentViewControlState
import data.LoadedDoc
import ui.dialogs.CreateFileDialog
import ui.dialogs.CreateFolderDialog
import ui.dialogs.DialogResult

const val REGISTER_INDENT = 10

@Composable
fun Register(
    info: DirectoryInformation,
    indent: Int,
    loaded: LoadedDoc,
    documentViewControlState: DocumentViewControlState
) {
    var dialog: @Composable ((((DialogResult, String) -> Unit)?) -> Unit)? by remember { mutableStateOf(null) }
    var dialogResult: ((DialogResult, String) -> Unit)? by remember { mutableStateOf(null) }

    ContextMenuArea(items = {
        listOf(
            ContextMenuItem("Add File") {
                dialog = CreateFileDialog
                dialogResult = { res, name ->
                    if (res == DialogResult.Ok) {
                        documentViewControlState.createDocument(info, name)
                        info.open.value = true
                        loaded.parent.value = info
                    }
                    dialogResult = null
                    dialog = null
                }
            },
            ContextMenuItem("Add Folder") {
                dialog = CreateFolderDialog
                dialogResult = { res, name ->
                    if (res == DialogResult.Ok) {
                        documentViewControlState.createFolder(info, name)
                    }
                    dialogResult = null
                    dialog = null
                }
            }
        )
    }) {
        if (dialog != null) dialog!!(dialogResult)

        Text(
            "${info.name}${if (info.directories.isNotEmpty()) " .v." else ""}",
            Modifier.padding(start = indent.dp, top = 2.dp, bottom = 2.dp).clickable {
                if (info.open.value) {
                    closeAllRegisters(info)
                    loaded.parent.value = info
                } else {
                    info.open.value = true
                    loaded.parent.value = info
                }
            })
    }
    if (info.open.value)
        Registers(info.directories, indent + REGISTER_INDENT, loaded, documentViewControlState)
}

fun closeAllRegisters(info: DirectoryInformation) {
    info.open.value = false
    info.directories.forEach {
        closeAllRegisters(it)
    }
}