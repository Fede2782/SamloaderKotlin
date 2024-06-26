package tk.zwander.commonCompose.view.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import tk.zwander.common.util.BifrostSettings
import tk.zwander.commonCompose.util.rememberThemeInfo
import tk.zwander.commonCompose.view.LocalUseMicaEffect

@Composable
fun BifrostTheme(block: @Composable () -> Unit) {
    val themeInfo = rememberThemeInfo()
    val useMicaEffect by BifrostSettings.Keys.useMicaEffect.collectAsMutableState()

    MaterialTheme(
        colorScheme = themeInfo.colors,
    ) {
        CompositionLocalProvider(
            LocalUseMicaEffect provides useMicaEffect,
        ) {
            block()
        }
    }
}
