package ui.documentView.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.DocumentInformation
import data.DocumentViewControlState
import kotlinx.coroutines.flow.first
import ui.PPCWindowState
import kotlin.math.exp


private val RegisterShape = GenericShape { size, _ ->
    val cornerRadius = (size.width + size.height) / 2
    lineTo(size.width - cornerRadius, 0f)
    cubicTo(size.width, 0f, size.width, 0f, size.width, cornerRadius)
    lineTo(size.width, size.height - cornerRadius)
    cubicTo(size.width, size.height, size.width, size.height, size.width - cornerRadius, size.height)
    lineTo(0f, size.height)
}

//private val LineShape = GenericShape { size, _ ->
//    lineTo(0f, size.height)
//    lineTo(0f, 0f)
//}

@Composable
fun SideBar(documentViewControlState: DocumentViewControlState, windowState: PPCWindowState) {
    val expanded = remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxHeight().background(Color(0xFF00FF00))
            .fillMaxWidth(0.2f + (if (expanded.value) 0.2f else 0f))
    ) {
        var activatedRegister by remember { mutableStateOf(-1) }

        val folders = windowState.application.loadDocumentInformation()
        // folder

        Column(Modifier.background(Color.White).fillMaxWidth()) {
            HeadBar()
            Row {
                Column(Modifier.fillMaxWidth(if (expanded.value) 0.5f else 1f)) {
                    folders.forEachIndexed { i, it ->

                        (Register(
                            it.name,
                            Color.Red,
                            documentViewControlState.loadedDoc.value == it.name,
                            it.children,
                            documentViewControlState,
                            expanded
                        ))
                    }
                }

                if (expanded.value) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight()
                            .drawBehind { drawLine(Color(0xFFE9E9E9), Offset(0f, 0f), Offset(0f, size.height), 3f) }) {
                        Text("lars", modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Register(
    name: String,
    color: Color,
    activated: Boolean = false,
    children: MutableList<DocumentInformation>,
    documentViewControlState: DocumentViewControlState,
    expanded: MutableState<Boolean>,
) {
    val height = 30.dp
    val lightColor = color.copy(alpha = 0.15f)
    Row(modifier = Modifier.padding(vertical = 10.dp).background(if (activated) lightColor else Color.Transparent)
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = color)
        ) {
            documentViewControlState.loadedDoc.value = name
            expanded.value = false
        }

    ) {

        Box(
            modifier = Modifier.size(20.dp, height).clip(RegisterShape).background(color)
        )
        Text(
            modifier = Modifier.height(height).padding(horizontal = 10.dp),
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(1.3f, TextUnitType.Em),
            textAlign = TextAlign.Center,
            color = Color(0xFF6B6B6B)
        )
    }
    if (activated) {
        children.forEach {
            Row(Modifier.fillMaxWidth().clickable { expanded.value = true }) {
                Text(text = it.name)
            }
        }
    }
}

@Composable
fun HeadBar() {
    Row(
        Modifier.background(Color(0xFFE9E9E9)).height(70.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar()
    }
}

@Composable
fun SearchBar() {
    val value = remember { mutableStateOf(TextFieldValue()) }
    val intSource = remember { MutableInteractionSource() }
    val focused = intSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    TextField(        value = value.value,
        onValueChange = {
            value.value = it

        }
    )
    BasicTextField(
        value = value.value,
        modifier = Modifier.fillMaxWidth(0.7f).fillMaxHeight(0.6f).clip(RoundedCornerShape(10.dp)),
        interactionSource = intSource,
        onValueChange = {
            value.value = it

        },
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color(0xFFCCCCCC)).clip(RoundedCornerShape(10.dp)), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Search, "", tint = Color(if (focused.value) 0xFF13C6FF else 0xFF6E6E6E))
                innerTextField()
            }
        }
    )
}