package data

import androidx.compose.ui.graphics.Color
import data.serializer.ColorSerializer
import kotlinx.serialization.Serializable

data class Pen(var color: Color, var width: Float)