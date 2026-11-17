package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lens
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import buenorigen.shared.generated.resources.Res
import buenorigen.shared.generated.resources.logo_solo
import cl.uchile.dcc.mobile.buenorigen.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun BuenOrigenMPApp(
    viewModel: MainViewModel
) {

    // NavController
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    // Items para Bottombar
    val items = listOf(
        ScreenRoutes.Home to "Inicio",
        ScreenRoutes.Catalog to "Catálogo",
        ScreenRoutes.Emprendedores to "Emprendedores",
        ScreenRoutes.Cart to "Carrito",
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                ) {
                Image(painterResource(Res.drawable.logo_solo),
                    contentDescription = "Logo BuenOrigen",
                    modifier = Modifier.size(120.dp))
                }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, (screen, title) ->
                    NavigationBarItem(
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, contentDescription = title)
                                1 -> Icon(Icons.Default.Search, contentDescription = title)
                                2 -> Icon(Icons.Default.Person, contentDescription = title)
                                3 -> Icon(Icons.Default.ShoppingCart, contentDescription = title)
                            }
                        },
                        label = { Text(title) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(screen.route) {
                                popUpTo(ScreenRoutes.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = ScreenRoutes.Home.route) {
                composable(ScreenRoutes.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable(ScreenRoutes.Cart.route) {
                    CartScreen(navController = navController)
                }
                composable(ScreenRoutes.Emprendedores.route) {
                    Emprendedores(navController = navController)
                }
                composable(ScreenRoutes.Catalog.route) {
                    Catalog(navController = navController)
                }
            }
        }
    }
}
