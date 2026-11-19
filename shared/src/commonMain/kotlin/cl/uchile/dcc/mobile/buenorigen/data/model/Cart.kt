package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: String,
    val productId: Long,
    val product: Product,
    val quantity: Int, // Max 3 units per product
    val unitPrice: Double
) {
    val subtotal: Double get() = unitPrice * quantity
    val estimatedCo2EvitadoKg: Double get() = product.co2EvitadoKg * quantity
    val estimatedGreenPoints: Int get() = product.greenPoints * quantity
}

@Serializable
data class Cart(
    val id: String,
    val customerId: String,
    val items: List<CartItem> = emptyList()
) {
    val totalItems: Int get() = items.sumOf { it.quantity }
    val subtotal: Double get() = items.sumOf { it.subtotal }
    val estimatedTotalCo2: Double get() = items.sumOf { it.estimatedCo2EvitadoKg }
    val estimatedTotalGreenPoints: Int get() = items.sumOf { it.estimatedGreenPoints }
}
