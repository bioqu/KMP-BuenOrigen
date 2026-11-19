package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.httpClient
import cl.uchile.dcc.mobile.buenorigen.data.model.PageResponse
import cl.uchile.dcc.mobile.buenorigen.data.model.Producer
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface ProducerRepository {
    suspend fun getProducers(): Result<List<Producer>>
    suspend fun getProducerById(id: String): Result<Producer>
}

class ProducerRepositoryImpl(
    private val client: io.ktor.client.HttpClient = httpClient
) : ProducerRepository {

    override suspend fun getProducers(): Result<List<Producer>> {
        return try {
            val page: PageResponse<Producer> = client.get("producers") {
                parameter("page", 0)
                parameter("size", 100)
            }.body()
            Result.success(page.content)
        } catch (e: Exception) {
            println("ERROR API producers: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getProducerById(id: String): Result<Producer> {
        return try {
            val response: Producer = client.get("producers/$id").body()
            Result.success(response)
        } catch (e: Exception) {
            println("ERROR API producer $id: ${e.message}")
            Result.failure(e)
        }
    }
}
