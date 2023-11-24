import javax.swing.JFileChooser
import java.io.File

object FileDialog {
    fun showOpenFileDialog(): File? {
        val fileChooser = JFileChooser()
        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.selectedFile
        }

        return null
    }
}