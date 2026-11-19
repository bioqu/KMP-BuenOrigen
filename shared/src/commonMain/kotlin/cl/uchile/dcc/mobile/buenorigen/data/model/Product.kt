package cl.uchile.dcc.mobile.buenorigen.data.model

import cl.uchile.dcc.mobile.buenorigen.data.publicFileContentUrl
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

/**
 * Producto público de la API.
 * El listado (`GET /products`) trae el resumen. El detalle agrega descripción e imágenes.
 */
@Serializable
data class Product(
    val id: Long,
    val name: String,
    val producerId: Long,
    val producerName: String = "",
    val categoryId: Long,
    val price: Double,
    val offerPrice: Double? = null,
    val finalPrice: Double = 0.0,
    val discountPercentage: Int = 0,
    val stock: Int = 0,
    val available: Boolean = true,
    val co2Avoided: Double = 0.0,
    val greenPoints: Int = 0,
    val rating: Double? = null,
    val reviewCount: Int = 0,
    val primaryImageFileId: String? = null,
    val description: String = "",
    val categoryName: String? = null,
    val imageFileIds: List<String> = emptyList()
) {
    val isActive: Boolean get() = available
    val isOnSale: Boolean get() = offerPrice != null
    val discountPrice: Double? get() = offerPrice
    val co2EvitadoKg: Double get() = co2Avoided
    val images: List<String>
        get() {
            val gallery = imageFileIds.mapNotNull { publicFileContentUrl(it) }
            if (gallery.isNotEmpty()) return gallery
            return listOfNotNull(publicFileContentUrl(primaryImageFileId))
        }
}
