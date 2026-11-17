package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Producer(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val bannerUrl: String? = null,
    val contactEmail: String,
    val phone: String,
    val address: String,
    val sustainabilityStory: String
)
