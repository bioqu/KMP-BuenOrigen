package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id: String,
    val street: String,
    val number: String,
    val apartment: String? = null,
    val comuna: String,
    val city: String,
    val region: String,
    val instructions: String? = null
)
