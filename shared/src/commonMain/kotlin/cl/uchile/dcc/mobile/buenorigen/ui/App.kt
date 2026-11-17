package cl.uchile.dcc.mobile.buenorigen.ui

import androidx.compose.runtime.*
import cl.uchile.dcc.mobile.buenorigen.data.repository.ProductRepositoryImpl
import cl.uchile.dcc.mobile.buenorigen.ui.screen.BuenOrigenMPApp
import cl.uchile.dcc.mobile.buenorigen.viewmodel.MainViewModel

@Composable
fun App() {
    BuenOrigenTheme {
        BuenOrigenMPApp(viewModel = remember { MainViewModel(ProductRepositoryImpl()) })
    }
}