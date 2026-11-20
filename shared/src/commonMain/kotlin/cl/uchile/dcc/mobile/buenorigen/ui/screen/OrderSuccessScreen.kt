package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.uchile.dcc.mobile.buenorigen.data.model.Order
import cl.uchile.dcc.mobile.buenorigen.data.repository.CartRepositoryImpl
import cl.uchile.dcc.mobile.buenorigen.data.repository.OrderRepositoryImpl
import cl.uchile.dcc.mobile.buenorigen.ui.PlatformVerticalScrollbar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSuccessScreen(
    navController: NavController,
    orderId: String
) {
    val orderRepository = remember { OrderRepositoryImpl(CartRepositoryImpl()) }
    var order by remember { mutableStateOf<Order?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    LaunchedEffect(orderId) {
        scope.launch {
            isLoading = true
            orderRepository.getOrderById(orderId).fold(
                onSuccess = {
                    order = it
                    isLoading = false
                },
                onFailure = {
                    errorMessage = it.message ?: "No se pudo cargar el pedido"
                    isLoading = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Confirmación de Compra") }
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
                order == null -> Text(text = "Pedido no encontrado")
                else -> {
                    val currentOrder = order!!
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(72.dp)
                            )
                            Text(
                                text = "¡Felicidades por tu compra!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Tu pedido #${currentOrder.id} ha sido procesado con éxito. 🌱 Gracias por elegir productos sostenibles.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "📦 Resumen del Pedido", style = MaterialTheme.typography.titleMedium)
                                    HorizontalDivider()
                                    Text(text = "Fecha: ${currentOrder.createdAt}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Dirección de envío: ${currentOrder.address.street}, ${currentOrder.address.comuna}, ${currentOrder.address.region}", style = MaterialTheme.typography.bodySmall)
                                    HorizontalDivider()
                                    Text(text = "Ítems comprados:", style = MaterialTheme.typography.titleSmall)
                                    currentOrder.items.forEach { detail ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "${detail.quantity}x ${detail.productName}", style = MaterialTheme.typography.bodyMedium)
                                            Text(text = "$${detail.subtotal}", style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                    HorizontalDivider()
                                    val totalCo2 = currentOrder.items.sumOf { it.totalCo2 }
                                    val totalPoints = currentOrder.items.sumOf { it.totalGreenPoints }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "CO₂ Evitado Total:", style = MaterialTheme.typography.bodyMedium)
                                        Text(text = "$totalCo2 kg", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Puntos Verdes Ganados:", style = MaterialTheme.typography.bodyMedium)
                                        Text(text = "+$totalPoints pts", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                                    }
                                    HorizontalDivider()
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Total Pagado:", style = MaterialTheme.typography.titleMedium)
                                        Text(text = "$${currentOrder.finalAmountPaid}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    navController.navigate(ScreenRoutes.Home.route) {
                                        popUpTo(ScreenRoutes.Home.route) { inclusive = true }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp)
                            ) {
                                Text(text = "Volver al Inicio")
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
