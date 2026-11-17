package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.httpClient
import cl.uchile.dcc.mobile.buenorigen.data.model.Producer
import io.ktor.client.call.body
import io.ktor.client.request.get

interface ProducerRepository {
    suspend fun getProducers(): Result<List<Producer>>
    suspend fun getProducerById(id: String): Result<Producer>
}

class ProducerRepositoryImpl(
    private val client: io.ktor.client.HttpClient = httpClient
) : ProducerRepository {

    private val mockProducers = listOf(
        Producer("p1", "Miel del Sur", "Apicultura sustentable y familiar de la Araucanía.", "url_logo_1", contactEmail = "contacto@mieldelsur.cl", phone = "+56911111111", address = "Temuco", sustainabilityStory = "Protección de abejas nativas."),
        Producer("p2", "Textil Nativo", "Ropa de algodón orgánico y fibras recicladas.", "url_logo_2", contactEmail = "contacto@textilnativ.cl", phone = "+56922222222", address = "Santiago", sustainabilityStory = "Cero desperdicio textil."),
        Producer("p3", "Cosmética Pura", "Cremas y jabones naturales cruelty-free.", "url_logo_3", contactEmail = "contacto@cosmeticapura.cl", phone = "+56933333333", address = "Valparaíso", sustainabilityStory = "Envases retornables y compostables."),
        Producer("p4", "Huerto Orgánico Andino", "Verduras y frutas cultivadas sin pesticidas químicos.", "url_logo_4", contactEmail = "contacto@huertoandino.cl", phone = "+56944444444", address = "La Serena", sustainabilityStory = "Agricultura regenerativa."),
        Producer("p5", "Café de Altura Ético", "Café de comercio justo y empaques biodegradables.", "url_logo_5", contactEmail = "contacto@cafeetico.cl", phone = "+56955555555", address = "Concepción", sustainabilityStory = "Comercio justo certificado."),
        Producer("p6", "Velas de Soja Artesanales", "Velas aromáticas con cera vegetal 100% natural.", "url_logo_6", contactEmail = "contacto@velassoja.cl", phone = "+56966666666", address = "Pucón", sustainabilityStory = "Sin parafina ni derivados del petróleo."),
        Producer("p7", "Muebles Madera Reciclada", "Muebles únicos hechos de maderas rescatadas.", "url_logo_7", contactEmail = "contacto@mueblesreciclados.cl", phone = "+56977777777", address = "Valdivia", sustainabilityStory = "Reutilización de maderas nativas."),
        Producer("p8", "Artesanía enreda", "Cestería y utensilios de fibras naturales locales.", "url_logo_8", contactEmail = "contacto@artesaniaenreda.cl", phone = "+56988888888", address = "Chiloé", sustainabilityStory = "Preservación de oficios ancestrales.")
    )

    override suspend fun getProducers(): Result<List<Producer>> {
        return try {
            val response: List<Producer> = client.get("producers").body()
            Result.success(response)
        } catch (_: Exception) {
            // Fallback seguro a datos mock si el servidor está abajo o inaccesible
            Result.success(mockProducers)
        }
    }

    override suspend fun getProducerById(id: String): Result<Producer> {
        return try {
            val response: Producer = client.get("producers/$id").body()
            Result.success(response)
        } catch (e: Exception) {
            val producer = mockProducers.find { it.id == id }
            if (producer != null) Result.success(producer) else Result.failure(e)
        }
    }
}
