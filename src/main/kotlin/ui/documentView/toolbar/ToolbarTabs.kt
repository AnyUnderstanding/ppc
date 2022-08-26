package ui.documentView.toolbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.*
import ui.documentView.toolbar.colorpicker.ColorSlider


val Tab1 = @Composable { documentViewControlState: DocumentViewControlState -> Tab1(documentViewControlState) }
val Tab2 = @Composable { documentViewControlState: DocumentViewControlState -> Tab2(documentViewControlState) }

@Composable
fun Tab1(documentViewControlState: DocumentViewControlState) {
    Row(modifier = Modifier.height(70.dp)) {
        ToolSelectButton(
            documentViewControlState.documentController.value.selectedTool.value is Eraser,
            "eraser.svg"
        ) {
            documentViewControlState.documentController.value.selectedTool.value = Eraser()
        }
        BarDivider()
        val tp: TPen? = if (documentViewControlState.documentController.value.selectedTool.value is TPen) (documentViewControlState.documentController.value.selectedTool.value as TPen) else null

        documentViewControlState.getPens().forEach {
            ToolSelectButton(
                tp?.pen === it,
                "coloredPen.svg"
            ) {
                documentViewControlState.documentController.value.selectedTool.value = TPen(it)
            }
        }



        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(false, "pen.svg", 20.dp, Modifier.height(40.dp)){
                val selectedPen: TPen? =
                    documentViewControlState.documentController.value.selectedTool.value as? TPen
                selectedPen?.let {

                    var penColor = Color.Red
                    documentViewControlState.activeDialog.value =
                        Triple(@Composable {
                            ColorSlider(documentViewControlState.documentController.value) { color ->
                                penColor = color
                                println(color)
                            }
                        }, "color picker") {
                            // finalize
                            it.pen.color = penColor
                        }
                }
            }
            IconButton(false, "add.svg", 20.dp, Modifier.height(40.dp)){
                var penColor = Color.Red
                documentViewControlState.activeDialog.value =
                    Triple(@Composable { ColorSlider(documentViewControlState.documentController.value) { color ->
                        penColor = color
                        println(color)
                    }
                    }, "color picker"){
                        // finalize
                        documentViewControlState.addPen(Pen(penColor, 1f))

                    }

            }
        }

        BarDivider()


        ToolSelectButton(
            documentViewControlState.documentController.value.selectedTool.value is Selector,
            "selection.svg"
        ) {
            documentViewControlState.documentController.value.selectedTool.value = Selector()
        }
    }


}

@Composable
fun Tab2(documentViewControlState: DocumentViewControlState) {
    Row {

        Button(onClick = { documentViewControlState.documentController.value.newPage() }) {
            Text("new Page")
        }
        Button(onClick = {
          //  documentViewControlState.activeDialog.value =
          //      Pair(@Composable { ColorSlider(documentViewControlState.documentController.value) }, "color picker")
        }) {
            Text("color picker")
        }
    }
}