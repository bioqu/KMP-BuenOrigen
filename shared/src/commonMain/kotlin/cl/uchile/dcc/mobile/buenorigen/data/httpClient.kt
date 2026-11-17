package cl.uchile.dcc.mobile.buenorigen.data

import cl.uchile.dcc.mobile.buenorigen.ui.apiBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    // URL base de tu API proporcionada por el usuario
    defaultRequest {
        url(apiBaseUrl)
    }

    // Configuración para convertir JSON automáticamente con kotlinx.serialization
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // Evita errores si la API devuelve campos nuevos
            isLenient = true
            prettyPrint = true
        })
    }
}
