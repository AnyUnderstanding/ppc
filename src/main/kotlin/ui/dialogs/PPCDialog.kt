package ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog

@Composable
fun InputDialog(title: String, fieldname: String, onResult: ((DialogResult, String) -> Unit)?) {
    var isOpen by remember { mutableStateOf(true) }
    var text by remember { mutableStateOf("") }

    fun result(result: DialogResult) {
        if (onResult != null) onResult(result, text)
        isOpen = false
    }

    if (isOpen) {
        Dialog({ result(DialogResult.Cancel) }, title = title) {
            Column {
                Row {
                    TextField(text, { text = it }, label = { Text(fieldname) })
                }
                Row {
                    Button({ result(DialogResult.Ok) }) { Text("Create") }
                    Button({ result(DialogResult.Cancel) }) { Text("Cancel") }
                }
            }
        }
    }
}

val CreateFileDialog = @Composable { onResult: ((DialogResult, String) -> Unit)? ->
    InputDialog("Create File", "File Name", onResult)
}

val CreateFolderDialog = @Composable { onResult: ((DialogResult, String) -> Unit)? ->
    InputDialog("Create Folder", "Folder Name", onResult)
}

enum class DialogResult {
    Ok,
    Cancel
}