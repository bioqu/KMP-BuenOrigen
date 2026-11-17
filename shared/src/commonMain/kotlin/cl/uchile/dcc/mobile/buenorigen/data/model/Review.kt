package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    val productId: String,
    val customerId: String,
    val customerName: String,
    val rating: Int, // 1 to 5
    val comment: String? = null,
    val createdAt: String
)
