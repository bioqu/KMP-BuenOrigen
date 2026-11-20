package cl.uchile.dcc.mobile.buenorigen.ui

import android.os.Build
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val apiBaseUrl: String = "http://159.112.150.143:3004/"

@Composable
actual fun PlatformVerticalScrollbar(
    listState: LazyListState,
    modifier: Modifier
) {
}

@Composable
actual fun PlatformVerticalScrollbar(
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    modifier: Modifier
) {
}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier
) {
}