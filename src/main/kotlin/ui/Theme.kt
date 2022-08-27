package ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

val THEME = mutableStateOf<Theme>(LightMode())

open class Theme {
    open val background: Color = Color(0xFFF8FCFF)
    open val mainColor: Color = Color(0xFFF0F0F0)
    open val secondaryColor: Color = Color(0xFFE9E9E9)
    open val toolIndicator: Color = Color(0xAA9C9C9C)
    open val selectorColor: Color = Color(0xAA7B7B7B)
    open val pageForeground: Color = Color.DarkGray // for example the lines on the page and the outline
    open val pageBackground: Color = Color.White
    open val textColor: Color = Color(0xFF6B6B6B)
    open val iconColor: Color = Color(0xFF6B6B6B)
    open val highlightColor: Color = Color(0xFF13C6FF)
    open val dividerColor: Color = Color(0xFFD8D8D8)
    open val textFieldColor: Color = Color(0xFFCCCCCC)
    open val menuBody: Color = Color.White

}

class LightMode : Theme() {

}

class DarkMode : Theme() {
    override val background = Color(0xFF575757)
    override val pageForeground: Color = Color.White
    override val pageBackground: Color = Color(0xFF363636)
    override val mainColor: Color = Color(0xFF898989)
    override val secondaryColor: Color = Color(0xFF5A5A5A)
    override val menuBody: Color = Color(0xFF898989)
    override val textColor: Color = Color(0xFFF2F2F2)
    override val iconColor: Color = Color(0xFFF2F2F2)
    override val highlightColor: Color = Color(0xFF1347FF)
    override val selectorColor: Color = Color(0xFFF2F2F2)




}