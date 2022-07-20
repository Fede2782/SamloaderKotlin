import androidx.compose.ui.window.Window
import kotlinx.cinterop.staticCFunction
import platform.AppKit.NSApp
import platform.AppKit.NSApplication
import platform.objc.objc_setUncaughtExceptionHandler
import tk.zwander.common.GradleConfig
import tk.zwander.commonCompose.MainView
import kotlin.time.ExperimentalTime

@Suppress("UNUSED_PARAMETER")
@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    setUnhandledExceptionHook {
        it.printStackTrace()
    }

    objc_setUncaughtExceptionHandler(staticCFunction<Any?, Unit> {
        println("Error $it")
    })

    NSApplication.sharedApplication()
    Window(GradleConfig.appName) {
        MainView()
    }
    NSApp?.run()
}