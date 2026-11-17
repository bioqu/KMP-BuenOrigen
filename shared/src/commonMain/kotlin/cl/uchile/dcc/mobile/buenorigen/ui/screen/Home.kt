package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cl.uchile.dcc.mobile.buenorigen.ui.Greeting

@Composable
fun HomeScreen(navController: NavController) {
    val greeting = Greeting().greet()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "¡Bienvenido a BuenOrigen! $greeting")
    }
}
