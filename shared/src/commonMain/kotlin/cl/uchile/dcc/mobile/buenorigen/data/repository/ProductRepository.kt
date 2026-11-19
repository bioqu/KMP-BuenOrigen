package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.httpClient
import cl.uchile.dcc.mobile.buenorigen.data.model.PageResponse
import cl.uchile.dcc.mobile.buenorigen.data.model.Product
import cl.uchile.dcc.mobile.buenorigen.data.model.SustainableAttributes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface ProductRepository {
    suspend fun getProducts(
        query: String? = null,
        categoryId: Long? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        onlyAvailable: Boolean = false,
        onlyOnSale: Boolean = false,
        sustainableAttributes: SustainableAttributes? = null,
        sortBy: String? = null // "price_asc", "price_desc", "name"
    ): Result<List<Product>>

    suspend fun getProductById(id: String): Result<Product>
    suspend fun getProductsByProducer(producerId: String): Result<List<Product>>
    suspend fun createProduct(product: Product): Result<Product>
    suspend fun updateProduct(product: Product): Result<Product>
}

class ProductRepositoryImpl(
    private val client: HttpClient = httpClient
) : ProductRepository {

    override suspend fun getProducts(
        query: String?,
        categoryId: Long?,
        minPrice: Double?,
        maxPrice: Double?,
        onlyAvailable: Boolean,
        onlyOnSale: Boolean,
        sustainableAttributes: SustainableAttributes?,
        sortBy: String?
    ): Result<List<Product>> {
        return try {
            val page: PageResponse<Product> = client.get("products") {
                parameter("page", 0)
                parameter("size", 100)
                if (!query.isNullOrBlank()) parameter("search", query)
                if (categoryId != null) parameter("categoryId", categoryId)
                if (minPrice != null) parameter("minPrice", minPrice)
                if (maxPrice != null) parameter("maxPrice", maxPrice)
                if (onlyAvailable) parameter("available", true)
                if (onlyOnSale) parameter("onSale", true)
                when (sortBy) {
                    "price_asc" -> {
                        parameter("sortBy", "finalPrice")
                        parameter("sortDirection", "ASC")
                    }
                    "price_desc" -> {
                        parameter("sortBy", "finalPrice")
                        parameter("sortDirection", "DESC")
                    }
                    "name" -> {
                        parameter("sortBy", "name")
                        parameter("sortDirection", "ASC")
                    }
                }
            }.body()
            Result.success(page.content)
        } catch (e: Exception) {
            println("ERROR API products: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getProductById(id: String): Result<Product> {
        return try {
            val response: Product = client.get("products/$id").body()
            Result.success(response)
        } catch (e: Exception) {
            println("ERROR API product $id: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getProductsByProducer(producerId: String): Result<List<Product>> {
        return try {
            val page: PageResponse<Product> = client.get("producers/$producerId/products") {
                parameter("page", 0)
                parameter("size", 100)
            }.body()
            Result.success(page.content)
        } catch (e: Exception) {
            println("ERROR API producer products $producerId: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun createProduct(product: Product): Result<Product> {
        return Result.failure(UnsupportedOperationException("Crear producto todavía no está conectado a la API"))
    }

    override suspend fun updateProduct(product: Product): Result<Product> {
        return Result.failure(UnsupportedOperationException("Actualizar producto todavía no está conectado a la API"))
    }
}
