package cl.uchile.dcc.mobile.buenorigen.data

import cl.uchile.dcc.mobile.buenorigen.ui.apiBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    expectSuccess = true

    defaultRequest {
        url(apiBaseUrl)
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 15_000
        requestTimeoutMillis = 20_000
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            explicitNulls = false
        })
    }
}

fun publicFileContentUrl(fileId: String?): String? {
    if (fileId.isNullOrBlank()) return null
    return apiBaseUrl.trimEnd('/') + "/files/$fileId/content"
}
