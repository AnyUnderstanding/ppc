package ui.documentView.sidebar

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeDialog
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.DocumentViewControlState
import ui.PPCWindowState
import ui.THEME

@Composable
fun SideBar(documentViewControlState: DocumentViewControlState, windowState: PPCWindowState) {
    val loaded = documentViewControlState.loadedDoc
    val document = documentViewControlState.document
    val hasFiles = loaded.value.parent.value.files.isNotEmpty()

    val barWidth = if (hasFiles) 0.4f else 0.2f

    println(windowState.window.size)

    Surface(
        Modifier.background(THEME.value.menuBody)
            .fillMaxHeight()
            .fillMaxWidth(barWidth)
    ) {
        Column(Modifier.background(THEME.value.menuBody).fillMaxWidth().fillMaxHeight()) {
            HeadBar(documentViewControlState)
            Row(Modifier.fillMaxHeight()) {
                Column(Modifier.fillMaxWidth(if (hasFiles) 0.5f else 1f).fillMaxHeight().clickable {
                    loaded.value.parent.value = document.value
                }) {
                    Registers(document.value.directories, 5, loaded.value, documentViewControlState)
                }

                if (hasFiles) {
                    Column(Modifier.fillMaxWidth(0.5f)
                        .fillMaxHeight()
                        .drawBehind {
                            drawLine(
                                THEME.value.dividerColor,
                                Offset(0f, 0f),
                                Offset(0f, size.height),
                                3f
                            )
                        })
                    {
                        loaded.value.parent.value.files.forEach {
                            Text(it.name, Modifier.padding(start = 5.dp).clickable {
                                documentViewControlState.loadDocument(it.path)
                                windowState.title.value = "PPC - ${it.local}"
                            })
                        }
                    }
                }
            }
        }
    }
}