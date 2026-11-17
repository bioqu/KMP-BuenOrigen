package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    PENDING, CONFIRMED, PREPARING, SHIPPED, DELIVERED, CANCELLED
}

@Serializable
data class OrderDetail(
    val id: String,
    val productId: String,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val historicalCo2Unit: Double,
    val historicalGreenPointsUnit: Int
) {
    val subtotal: Double get() = unitPrice * quantity
    val totalCo2: Double get() = historicalCo2Unit * quantity
    val totalGreenPoints: Int get() = historicalGreenPointsUnit * quantity
}

@Serializable
data class Order(
    val id: String,
    val customerId: String,
    val address: Address,
    val items: List<OrderDetail>,
    val totalAmount: Double,
    val status: OrderStatus,
    val createdAt: String,
    val pointsUsedForDiscount: Int = 0,
    val finalAmountPaid: Double
)
