import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import pdf.TextReplacer
import java.io.File

@Composable
@Preview
fun App() {
    var screen by remember { mutableStateOf("start") }
    var globalFile: File? = null

    MaterialTheme {
        when (screen) {
            "start" -> {
                Button(onClick = {
                    FileDialog.showOpenFileDialog()?.apply {
                        globalFile = this
                        screen = "file"
                    }
                }) {
                    Text("select file")
                }
            }

            "file" -> {
                globalFile?.let { processDataApplaying(it) {
                    screen = "start"
                }
                }
            }
        }
    }
}

@Composable
fun processDataApplaying(file: File, callback: () -> Unit) {
    Column {
        val companyLink = remember { mutableStateOf(TextFieldValue()) }
        val companyName = remember { mutableStateOf(TextFieldValue()) }

        Row {
            Text("Link to offer")
            TextField(companyLink.value, onValueChange = {
                companyLink.value = it
            })
        }

        Row {
            Text("Company name")
            TextField(companyName.value, onValueChange = {
                companyName.value = it
            })
        }

        Row {
            Button(onClick = {
                val textReplacer = TextReplacer(
                    file.path,
                    companyLink.value.text,
                    companyName.value.text)

                textReplacer.replaceText(file.path)

                callback()
            }) {
                Text("Prepare pdf")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
