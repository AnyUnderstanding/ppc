package control

import androidx.compose.ui.geometry.Offset

interface InputHandler {
    fun inputDown(position: Offset)
    fun inputUp(position: Offset)
    fun inputMoved(position: Offset)
}