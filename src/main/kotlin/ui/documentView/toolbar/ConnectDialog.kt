package ui.documentView.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import control.remoteclient.ConnectionController

@Composable
fun ConnectDialog(connectionController: ConnectionController) {
    val sessionID = remember { mutableStateOf(TextFieldValue()) }
    val name = remember { mutableStateOf(TextFieldValue()) }
    val nameFormatRegex = Regex("(?=.{3,32}\$)[A-Za-z0-9]+(?:\\s[A-Za-z0-9]+)?")
    val uuidV4Regex = Regex("[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-4[0-9A-Fa-f]{3}-[89ABab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}")

    Column(Modifier.padding(50.dp)) {
        val matchingSessionID = inputElement("Gib deine SessionID ein", sessionID, inputCheck = uuidV4Regex)

        val matchingName = inputElement("Wie willst du gennant werden?", name, inputCheck = nameFormatRegex)
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    connectionController.connect(
                        sessionID = sessionID.value.text.trim(),
                        name =  name.value.text.trim()
                    )
                },
                enabled = matchingSessionID && matchingName,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF13C6FF),
                    contentColor = Color.White
                )
            ) {
                Text("Verbinden")
            }
        }
    }
}

@Composable
fun inputElement(caption: String, buffer: MutableState<TextFieldValue>, inputCheck: Regex? = null): Boolean {
    Text(caption)
    var match = false
    if (inputCheck == null) {
        TextField(
            value = buffer.value,
            onValueChange = { buffer.value = it },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF13C6FF),
                cursorColor = Color(0xFF13C6FF)
            )
        )
    } else {
        TextField(
            modifier = Modifier.fillMaxWidth(0.5f),
            value = buffer.value,
            onValueChange = { buffer.value = it },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF13C6FF),
                cursorColor = Color(0xFF13C6FF)
            ),
            trailingIcon = {
                if (inputCheck.matches(buffer.value.text.trim())) {
                    Icon(Icons.Filled.Check, "", tint = Color(0xFF13C6FF))
                    match = true
                } else {
                    Icon(Icons.Filled.Close, "", tint = Color(0xFF6E6E6E))
                }
            }

        )
    }
    return match
}