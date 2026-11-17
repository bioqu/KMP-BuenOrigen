package cl.uchile.dcc.mobile.buenorigen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.buenorigen.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import cl.uchile.dcc.mobile.buenorigen.data.repository.ProductRepository

sealed interface ProductUiState {
    data object Loading : ProductUiState
    data class Success(val products: List<Product>) : ProductUiState
    data class Error(val message: String) : ProductUiState
}

class MainViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = ProductUiState.Loading

            try {
                repository.getProducts().fold(
                    onSuccess = { productos ->
                        _uiState.value = ProductUiState.Success(productos)
                    },
                    onFailure = { error ->
                        _uiState.value = ProductUiState.Error(
                            error.message ?: "No se pudieron cargar los productos"
                        )
                    }
                )
            } catch (error: Exception) {
                _uiState.value = ProductUiState.Error(
                    error.message ?: "Ocurrió un error inesperado"
                )
            }
        }
    }
}