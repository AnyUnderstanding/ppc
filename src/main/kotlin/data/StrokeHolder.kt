package data

import androidx.compose.ui.graphics.Color

interface StrokeHolder {
    fun addStroke(color: Color, width: Float): Stroke
    fun addStrokes(strokesToAdd: List<Stroke>)
    fun removeStrokes(strokesToDelete: List<Stroke>)
}