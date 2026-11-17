package cl.uchile.dcc.mobile.buenorigen.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.uchile.dcc.mobile.buenorigen.data.model.Producer
import cl.uchile.dcc.mobile.buenorigen.data.repository.ProducerRepositoryImpl
import cl.uchile.dcc.mobile.buenorigen.ui.component.ProducerAvatar
import kotlinx.coroutines.launch

@Composable
fun Emprendedores(navController: NavController) {
    val repository = remember { ProducerRepositoryImpl() }
    var producers by remember { mutableStateOf<List<Producer>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            repository.getProducers().fold(
                onSuccess = { list ->
                    producers = list
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error.message
                    isLoading = false
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text(text = "Error: $errorMessage")
            producers.isEmpty() -> Text(text = "No hay emprendedores disponibles")
            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(producers) { producer ->
                        ProducerAvatar(
                            producer = producer,
                            onClick = { /* Navegar o ver detalle */ },
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }
            }
        }
    }
}
