package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String? = null
)
