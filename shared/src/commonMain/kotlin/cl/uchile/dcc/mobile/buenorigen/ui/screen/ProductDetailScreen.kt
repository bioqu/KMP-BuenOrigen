package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String
) {
    val productRepository = remember { ProductRepositoryImpl() }
    val cartRepository = remember { CartRepositoryImpl() }
    val customerId = "customer-1"

    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        scope.launch {
            isLoading = true
            productRepository.getProductById(productId).fold(
                onSuccess = {
                    product = it
                    isLoading = false
                },
                onFailure = {
                    errorMessage = it.message ?: "Error al cargar producto"
                    isLoading = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product?.name ?: "Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                product == null -> Text(text = "Producto no encontrado")
                else -> {
                    val currentProduct = product!!
                    val scrollState = rememberScrollState()

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AsyncImage(
                                model = currentProduct.images.firstOrNull() ?: "",
                                contentDescription = currentProduct.name,
                                contentScale = ContentScale.Fit,
                                placeholder = painterResource(Res.drawable.fondo_mobile),
                                error = painterResource(Res.drawable.fondo_mobile),
                                fallback = painterResource(Res.drawable.fondo_mobile),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp)
                            )

                            if (successMessage != null) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = successMessage!!,
                                        modifier = Modifier.padding(12.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Text(
                                text = currentProduct.name,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    if (currentProduct.isOnSale) {
                                        Text(
                                            text = "$${currentProduct.price.toLong()}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "$${currentProduct.finalPrice.toLong()}",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        Text(
                                            text = "$${currentProduct.finalPrice.toLong()}",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                if (currentProduct.producerName.isNotBlank()) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.small
                                    ) {
                                        Text(
                                            text = "Productor: ${currentProduct.producerName}",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }

                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "🌱 Impacto Sostenible",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "• CO₂ evitado: ${currentProduct.co2EvitadoKg} kg",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "• Puntos verdes: +${currentProduct.greenPoints} pts",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "• Stock disponible: ${currentProduct.stock} unidades",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            if (currentProduct.description.isNotBlank()) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(text = "Descripción", style = MaterialTheme.typography.titleMedium)
                                    Text(text = currentProduct.description, style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Selector de cantidad y Botón Agregar al Carrito
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Cantidad:", style = MaterialTheme.typography.titleMedium)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { if (quantity > 1) quantity-- },
                                        enabled = quantity > 1
                                    ) {
                                        Text("-")
                                    }
                                    Text(text = "$quantity", style = MaterialTheme.typography.titleMedium)
                                    Button(
                                        onClick = { if (quantity < 3 && quantity < currentProduct.stock) quantity++ },
                                        enabled = quantity < 3 && quantity < currentProduct.stock
                                    ) {
                                        Text("+")
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        cartRepository.addToCart(customerId, currentProduct, quantity).fold(
                                            onSuccess = {
                                                successMessage = "¡Agregado al carrito con éxito! ($quantity unidades)"
                                            },
                                            onFailure = { err ->
                                                errorMessage = err.message ?: "No se pudo agregar al carrito"
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                enabled = currentProduct.isActive && currentProduct.stock > 0
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (currentProduct.stock > 0) "Agregar al Carrito" else "Sin Stock")
                            }
                        }
                        PlatformVerticalScrollbar(
                            scrollState = scrollState,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}
