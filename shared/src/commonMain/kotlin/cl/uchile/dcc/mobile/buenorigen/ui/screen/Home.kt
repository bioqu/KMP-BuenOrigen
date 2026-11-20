package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import buenorigen.shared.generated.resources.Res
import buenorigen.shared.generated.resources.fondo_mobile
import coil3.compose.AsyncImage
import cl.uchile.dcc.mobile.buenorigen.data.model.Product
import cl.uchile.dcc.mobile.buenorigen.data.repository.CartRepositoryImpl
import cl.uchile.dcc.mobile.buenorigen.data.repository.ProductRepositoryImpl
import cl.uchile.dcc.mobile.buenorigen.ui.PlatformVerticalScrollbar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(navController: NavController) {
    val productRepository = remember { ProductRepositoryImpl() }
    val cartRepository = remember { CartRepositoryImpl() }
    val customerId = "customer-1"

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        scope.launch {
            productRepository.getProducts().fold(
                onSuccess = { list ->
                    products = list
                    isLoading = false
                },
                onFailure = { err ->
                    errorMessage = err.message ?: "Error al cargar productos"
                    isLoading = false
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
            products.isEmpty() -> {
                Text(
                    text = "No hay productos destacados disponibles",
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(
                        text = "🌱 Productos Destacados",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Con impacto positivo y producidos localmente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (successMessage != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = successMessage!!,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(products) { product ->
                            Card(
                                onClick = {
                                    navController.navigate(ScreenRoutes.ProductDetail.createRoute(product.id))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    AsyncImage(
                                        model = product.images.firstOrNull() ?: "",
                                        contentDescription = product.name,
                                        contentScale = ContentScale.Fit,
                                        placeholder = painterResource(Res.drawable.fondo_mobile), // Se muestra mientras carga
                                        error = painterResource(Res.drawable.fondo_mobile),       // Se muestra si falla al cargar
                                        fallback = painterResource(Res.drawable.fondo_mobile),    // Se muestra si la URL está vacía
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(110.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = product.producerName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (product.isOnSale) {
                                            "$${product.finalPrice.toLong()} (antes $${product.price.toLong()})"
                                        } else {
                                            "$${product.finalPrice.toLong()}"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "CO₂: ${product.co2EvitadoKg}kg | +${product.greenPoints}pts",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                cartRepository.addToCart(customerId, product, 1).fold(
                                                    onSuccess = {
                                                        successMessage = "¡${product.name} agregado al carrito!"
                                                    },
                                                    onFailure = { err ->
                                                        errorMessage = err.message
                                                    }
                                                )
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().height(36.dp),
                                        contentPadding = PaddingValues(4.dp)
                                    ) {
                                        Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Agregar", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        PlatformVerticalScrollbar(
            gridState = gridState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}
