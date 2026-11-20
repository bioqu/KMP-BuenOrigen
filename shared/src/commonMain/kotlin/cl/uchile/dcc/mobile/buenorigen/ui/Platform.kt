package cl.uchile.dcc.mobile.buenorigen.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val apiBaseUrl: String

// Vertical Scroll para PC
@Composable
expect fun PlatformVerticalScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier
)

@Composable
expect fun PlatformVerticalScrollbar(
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState,
    modifier: Modifier = Modifier
)

@Composable
expect fun PlatformVerticalScrollbar(
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier
)