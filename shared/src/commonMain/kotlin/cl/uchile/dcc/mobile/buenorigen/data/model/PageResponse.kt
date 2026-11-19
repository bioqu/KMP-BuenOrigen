package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val page: Int = 0,
    val size: Int = 0,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val sortBy: String? = null,
    val sortDirection: String? = null
)
