package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import cl.uchile.dcc.mobile.buenorigen.data.model.Cart
import cl.uchile.dcc.mobile.buenorigen.data.repository.CartRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun CartScreen(navController: NavController) {
    val cartRepository = remember { CartRepositoryImpl() }
    val customerId = "customer-1" // Usuario actual por defecto
    var cart by remember { mutableStateOf<Cart?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun loadCart() {
        scope.launch {
            isLoading = true
            cartRepository.getCart(customerId).fold(
                onSuccess = {
                    cart = it
                    isLoading = false
                },
                onFailure = {
                    errorMessage = it.message
                    isLoading = false
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        loadCart()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            cart == null || cart!!.items.isEmpty() -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Tu carrito está vacío 🛒", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Agrega productos sostenibles desde el catálogo", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            else -> {
                val currentCart = cart!!
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text(text = "Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (successMessage != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = successMessage!!,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(currentCart.items) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = item.product.images.firstOrNull() ?: "",
                                        contentDescription = item.product.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(70.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.product.name, style = MaterialTheme.typography.titleMedium)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = "$${item.unitPrice} c/u", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "CO₂ evitado: ${item.estimatedCo2EvitadoKg} kg | Puntos: +${item.estimatedGreenPoints}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                    }

                                    // Cantidad y controles (+ / - / eliminar)
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        cartRepository.updateQuantity(customerId, item.productId, item.quantity - 1).fold(
                                                            onSuccess = { cart = it },
                                                            onFailure = { errorMessage = it.message }
                                                        )
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Text("-", style = MaterialTheme.typography.titleMedium)
                                            }
                                            Text(text = "${item.quantity}", style = MaterialTheme.typography.bodyLarge)
                                            IconButton(
                                                onClick = {
                                                    scope.launch {
                                                        cartRepository.updateQuantity(customerId, item.productId, item.quantity + 1).fold(
                                                            onSuccess = { cart = it },
                                                            onFailure = { errorMessage = it.message }
                                                        )
                                                    }
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Text("+", style = MaterialTheme.typography.titleMedium)
                                            }
                                        }
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    cartRepository.removeFromCart(customerId, item.productId).fold(
                                                        onSuccess = { cart = it },
                                                        onFailure = { errorMessage = it.message }
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Total ítems:", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "${currentCart.totalItems}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "CO₂ Total Evitado:", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "${currentCart.estimatedTotalCo2} kg", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Puntos Verdes Estimados:", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "+${currentCart.estimatedTotalGreenPoints} pts", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Total a Pagar:", style = MaterialTheme.typography.titleLarge)
                                Text(text = "$${currentCart.subtotal}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        cartRepository.clearCart(customerId).fold(
                                            onSuccess = {
                                                cart = it
                                                successMessage = "¡Pedido confirmado con éxito! 🌱 Gracias por comprar sostenible."
                                            },
                                            onFailure = { errorMessage = it.message }
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Confirmar Compra (Checkout)")
                            }
                        }
                    }
                }
            }
        }
    }
}
