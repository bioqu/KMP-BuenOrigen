package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SustainableAttributes(
    val isVegan: Boolean = false,
    val isCrueltyFree: Boolean = false,
    val isRecycledMaterial: Boolean = false,
    val isReusable: Boolean = false,
    val isPlasticFree: Boolean = false,
    val isOrganicCertified: Boolean = false,
    val isCompostableCertified: Boolean = false
)

@Serializable
data class Product(
    val id: String,
    val producerId: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val images: List<String>,
    val co2EvitadoKg: Double, // kg de CO2 evitado por unidad
    val greenPoints: Int, // Calculado según CO2 evitado o precio según regla (100 pesos = 1 punto, o 100 puntos por kg CO2e)
    val isActive: Boolean = true,
    val isOnSale: Boolean = false,
    val discountPrice: Double? = null,
    val sustainableAttributes: SustainableAttributes = SustainableAttributes(),
    // Atributos diferenciadores opcionales
    val size: String? = null,
    val weightKg: Double? = null,
    val dimensions: String? = null,
    val color: String? = null,
    val material: String? = null
)
