package util

import androidx.compose.ui.graphics.Color

class ColorVector(val alpha: Float, val red: Float, val green: Float, val blue: Float) {

    operator fun plus(other: ColorVector): ColorVector {
        val a = alpha + other.alpha
        val r = red + other.red
        val g = green + other.green
        val b = blue + other.blue

        return ColorVector(a, r, g, b)
    }

    operator fun minus(other: ColorVector): ColorVector {
        val a = alpha - other.alpha
        val r = red - other.red
        val g = green - other.green
        val b = blue - other.blue

        return ColorVector(a, r, g, b)
    }

    operator fun times(other: Float): ColorVector {
        val a = alpha * other
        val r = red * other
        val g = green * other
        val b = blue * other

        return ColorVector(a, r, g, b)
    }

    fun toColor() = Color(red,green,blue, alpha)
}