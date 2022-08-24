package ui.documentView.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import data.*
import ui.PPCWindowState
import ui.documentView.toolbar.IconButton


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

@OptIn(ExperimentalUnitApi::class)
@Composable
fun SideBar(documentViewControlState: DocumentViewControlState, windowState: PPCWindowState) {
    val expanded = remember { mutableStateOf(false) }
    // Box(modifier = Modifier.fillMaxSize().clickable { documentViewControlState.sideBarActivated.value = false })
    Surface(
        modifier = Modifier.fillMaxHeight().background(Color(0xFF00FF00))
            .fillMaxWidth(0.2f + (if (expanded.value) 0.2f else 0f))
    ) {
        var activatedRegister by remember { mutableStateOf(-1) }

//        val folders = windowState.folders
        // folder

        Column(Modifier.background(Color.White).fillMaxWidth()) {
            HeadBar(documentViewControlState)
            Row {
                Column(Modifier.fillMaxWidth(if (expanded.value) 0.5f else 1f)) {
                    windowState.folders.value.forEachIndexed { i, it ->

                        Register(
                            it,
                            Color.Red,
                            documentViewControlState.loadedDoc.value.workbook.value?.name == it.name,
                            it.children,
                            documentViewControlState,
                            expanded,
                            windowState
                        )
                    }
                    AddButton(windowState, null)
                }

                if (expanded.value) {
                    Column(
                        modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight().fillMaxWidth()
                            .drawBehind { drawLine(Color(0xFFE9E9E9), Offset(0f, 0f), Offset(0f, size.height), 3f) }) {
                        documentViewControlState.loadedDoc.value.folder.value?.children?.forEach {

                            Row(Modifier.fillMaxWidth().clickable {
                                expanded.value = true
                            }.fillMaxWidth()) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier.padding(30.dp, 10.dp),
                                    fontSize = TextUnit(1f, TextUnitType.Em),
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF6B6B6B)
                                )

                            }
                        }
                        AddButton(windowState, documentViewControlState.loadedDoc.value.folder.value, true)

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun Register(
    doc: DocumentInformation,
    color: Color,
    activated: Boolean = false,
    children: MutableList<DocumentInformation>,
    documentViewControlState: DocumentViewControlState,
    expanded: MutableState<Boolean>,
    windowState: PPCWindowState
) {
    val height = 30.dp
    val lightColor = color.copy(alpha = 0.15f)
    Row(modifier = Modifier.padding(vertical = 10.dp).background(if (activated) lightColor else Color.Transparent)
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = color)
        ) {
            documentViewControlState.loadedDoc.value.workbook.value = doc
            expanded.value = false
        }

    ) {

        Box(
            modifier = Modifier.size(20.dp, height).clip(RegisterShape).background(color)
        )
        Text(
            modifier = Modifier.height(height).padding(horizontal = 10.dp),
            text = doc.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextUnit(1.3f, TextUnitType.Em),
            textAlign = TextAlign.Center,
            color = Color(0xFF6B6B6B)
        )
    }
    if (activated) {
        children.forEach {
            Row(Modifier.fillMaxWidth().clickable {
                expanded.value = true
                documentViewControlState.loadedDoc.value.folder.value = it
            }) {
                Text(
                    text = it.name,
                    modifier = Modifier.padding(30.dp, 10.dp),
                    fontSize = TextUnit(1f, TextUnitType.Em),
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6B6B6B)
                )
                if (it.type == DocumentInformationType.Folder) Icon(Icons.Filled.KeyboardArrowDown, "")
            }
        }

        AddButton(windowState, doc)
    }
}

@Composable
fun HeadBar(documentViewControlState: DocumentViewControlState) {
    Row(
        Modifier.background(Color(0xFFE9E9E9)).height(70.dp).fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(false, "newFile.svg")
            IconButton(false, "leftArrow.svg") { documentViewControlState.sideBarActivated.value = false }
        }
    }
}

@Composable
fun SearchBar() {
    val value = remember { mutableStateOf(TextFieldValue()) }
    val intSource = remember { MutableInteractionSource() }
    val focused = intSource.collectIsFocusedAsState()

    BasicTextField(
        value = value.value,
        modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(0.75f).clip(RoundedCornerShape(15.dp)),
        interactionSource = intSource,
        onValueChange = {
            value.value = it
        },
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color(0xFFCCCCCC))
                    .clip(RoundedCornerShape(10.dp)), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Search,
                    "",
                    tint = Color(/*if (focused.value) 0xFF13C6FF else*/ 0xFF6B6B6B),
                    modifier = Modifier.padding(5.dp)
                )
                innerTextField()
            }
        }
    )
}


@Composable
fun AddButton(
    windowState: PPCWindowState,
    parent: DocumentInformation?,
    file: Boolean = false
) {
    Button({
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val t = List(20) { charset.random() }.joinToString("")
        if (file) windowState.application.newFile(parent, t)
        else windowState.application.newFolder(parent, t)

        windowState.updateDocs()
    }) {
        Text("Add")
    }
}