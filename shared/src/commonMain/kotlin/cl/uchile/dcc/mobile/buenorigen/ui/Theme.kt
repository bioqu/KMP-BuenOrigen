package cl.uchile.dcc.mobile.buenorigen.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Define tus colores personalizados (ej. tonos verdes acordes a "Buen Origen")
val VerdePrimario = Color(0xFF2E7D32)
val VerdeClaroContainer = Color(0xFFC8E6C9)
val FondoApp = Color(0xFFFAFAFA)

// 2. Crea tu esquema de colores
private val LightColors = lightColorScheme(
    primary = VerdePrimario,
    primaryContainer = VerdeClaroContainer,
    background = FondoApp,
    surface = Color.White,
    // Puedes personalizar más colores aquí (secondary, tertiary, error, etc.)
)

// 3. Crea tu función Composable de Tema
@Composable
fun BuenOrigenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = MaterialTheme.typography, // Puedes personalizar tipografías también
        content = content
    )
}