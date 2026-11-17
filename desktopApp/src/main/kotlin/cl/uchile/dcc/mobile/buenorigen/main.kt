package cl.uchile.dcc.mobile.buenorigen

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cl.uchile.dcc.mobile.buenorigen.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BuenOrigen",
    ) {
        App()
    }
}