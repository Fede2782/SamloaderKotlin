package tk.zwander.common.view

import com.soywiz.korio.stream.toAsync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import tk.zwander.common.data.DecryptFileInfo
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import tk.zwander.common.util.toAsync as toAsync1

actual object PlatformDecryptView {
    actual suspend fun getInput(callback: suspend CoroutineScope.(DecryptFileInfo?) -> Unit) {
        coroutineScope {
            val dialog = FileDialog(Frame())
            dialog.mode = FileDialog.LOAD
            dialog.isVisible = true

            if (dialog.file != null) {
                val input = File(dialog.directory, dialog.file)

                callback(
                    DecryptFileInfo(
                        input.name,
                        input.absolutePath,
                        input.inputStream().toAsync(),
                        input.length(),
                        File(input.parentFile, input.nameWithoutExtension).outputStream().toAsync1()
                    )
                )
            }
        }
    }
}