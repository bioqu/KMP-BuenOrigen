package cl.uchile.dcc.mobile.buenorigen.data.model

import cl.uchile.dcc.mobile.buenorigen.data.publicFileContentUrl
import kotlinx.serialization.Serializable

/**
 * Emprendedor público.
 * El listado trae id, businessName, description, logoFileId y location.
 * El detalle agrega bannerFileId y websiteUrl.
 */
@Serializable
data class Producer(
    val id: Long,
    val businessName: String,
    val description: String? = null,
    val logoFileId: String? = null,
    val location: String? = null,
    val bannerFileId: String? = null,
    val websiteUrl: String? = null
) {
    val name: String get() = businessName
    val logoUrl: String get() = publicFileContentUrl(logoFileId).orEmpty()
    val bannerUrl: String? get() = publicFileContentUrl(bannerFileId)
}
