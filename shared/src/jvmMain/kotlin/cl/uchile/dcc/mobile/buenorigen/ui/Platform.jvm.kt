package cl.uchile.dcc.mobile.buenorigen.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val apiBaseUrl: String = System.getenv("API_BASE_URL") ?: "http://159.112.150.143:3004/"

@Composable
actual fun PlatformVerticalScrollbar(
    listState: LazyListState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(listState),
        modifier = modifier
    )
}

@Composable
actual fun PlatformVerticalScrollbar(
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(gridState),
        modifier = modifier
    )
}

@Composable
actual fun PlatformVerticalScrollbar(
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        modifier = modifier
    )
}