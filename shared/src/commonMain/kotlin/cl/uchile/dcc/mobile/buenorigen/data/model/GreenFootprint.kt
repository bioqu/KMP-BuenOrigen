package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class MovementType {
    WELCOME_BONUS, PURCHASE_EARNED, REDEMPTION, REFUND, REVIEW_BONUS
}

@Serializable
data class GreenPointMovement(
    val id: String,
    val customerId: String,
    val orderId: String? = null,
    val points: Int, // Positive or negative
    val type: MovementType,
    val description: String,
    val createdAt: String
)

@Serializable
data class GreenFootprintSummary(
    val customerId: String,
    val totalGreenPointsBalance: Int,
    val totalCo2EvitadoKg: Double,
    val movements: List<GreenPointMovement>
)
