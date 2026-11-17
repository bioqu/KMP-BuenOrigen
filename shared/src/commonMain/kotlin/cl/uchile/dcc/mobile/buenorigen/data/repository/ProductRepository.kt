package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.httpClient
import cl.uchile.dcc.mobile.buenorigen.data.model.Product
import cl.uchile.dcc.mobile.buenorigen.data.model.SustainableAttributes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface ProductRepository {
    suspend fun getProducts(
        query: String? = null,
        categoryId: String? = null,
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
    private val mockProducts = mutableListOf(
        Product(
            id = "prod-1",
            producerId = "p1",
            categoryId = "cat-1",
            name = "Miel Orgánica Multifloral 500g",
            description = "Miel pura de abejas nativas recolectada en bosques de la Araucanía.",
            price = 6500.0,
            stock = 15,
            images = listOf("url_miel_1"),
            co2EvitadoKg = 1.2,
            greenPoints = 120,
            isActive = true,
            sustainableAttributes = SustainableAttributes(isOrganicCertified = true, isPlasticFree = true)
        ),
        Product(
            id = "prod-2",
            producerId = "p2",
            categoryId = "cat-2",
            name = "Polera de Algodón Orgánico Talla M",
            description = "Polera 100% algodón orgánico, teñida con tintes naturales.",
            price = 18990.0,
            stock = 8,
            images = listOf("url_polera_1"),
            co2EvitadoKg = 3.5,
            greenPoints = 350,
            isActive = true,
            isOnSale = true,
            discountPrice = 15000.0,
            sustainableAttributes = SustainableAttributes(isVegan = true, isOrganicCertified = true),
            size = "M",
            color = "Verde Olivo"
        ),
        Product(
            id = "prod-3",
            producerId = "p3",
            categoryId = "cat-3",
            name = "Jabón Sólido de Caléndula",
            description = "Jabón artesanal hidratante, sin envase plástico.",
            price = 4500.0,
            stock = 0, // Sin stock
            images = listOf("url_jabon_1"),
            co2EvitadoKg = 0.8,
            greenPoints = 80,
            isActive = true,
            sustainableAttributes = SustainableAttributes(isCrueltyFree = true, isPlasticFree = true, isVegan = true)
        )
    )

    override suspend fun getProducts(
        query: String?,
        categoryId: String?,
        minPrice: Double?,
        maxPrice: Double?,
        onlyAvailable: Boolean,
        onlyOnSale: Boolean,
        sustainableAttributes: SustainableAttributes?,
        sortBy: String?
    ): Result<List<Product>> {
        return try {
            val response: List<Product> = client.get("products").body()
            Result.success(response)
        } catch (_: Exception) {
            var filtered = mockProducts.filter { it.isActive }

            if (!query.isNullOrBlank()) {
                filtered = filtered.filter { it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }
            }
            if (!categoryId.isNullOrBlank()) {
                filtered = filtered.filter { it.categoryId == categoryId }
            }
            if (minPrice != null) {
                filtered = filtered.filter { (it.discountPrice ?: it.price) >= minPrice }
            }
            if (maxPrice != null) {
                filtered = filtered.filter { (it.discountPrice ?: it.price) <= maxPrice }
            }
            if (onlyAvailable) {
                filtered = filtered.filter { it.stock > 0 }
            }
            if (onlyOnSale) {
                filtered = filtered.filter { it.isOnSale }
            }

            Result.success(filtered)
        }
    }

    override suspend fun getProductById(id: String): Result<Product> {
        return try {
            val response: Product = client.get("products/$id").body()
            Result.success(response)
        } catch (_: Exception) {
            val product = mockProducts.find { it.id == id }
            if (product != null) Result.success(product) else Result.failure(Exception("Producto no encontrado"))
        }
    }

    override suspend fun getProductsByProducer(producerId: String): Result<List<Product>> {
        return try {
            val response: List<Product> = client.get("producers/$producerId/products").body()
            Result.success(response)
        } catch (_: Exception) {
            Result.success(mockProducts.filter { it.producerId == producerId })
        }
    }

    override suspend fun createProduct(product: Product): Result<Product> {
        return try {
            val response: Product = client.get("products").body() // placeholder or post
            Result.success(response)
        } catch (_: Exception) {
            mockProducts.add(product)
            Result.success(product)
        }
    }

    override suspend fun updateProduct(product: Product): Result<Product> {
        return try {
            Result.success(product)
        } catch (_: Exception) {
            val index = mockProducts.indexOfFirst { it.id == product.id }
            if (index != -1) {
                mockProducts[index] = product
                Result.success(product)
            } else {
                Result.failure(Exception("Producto no encontrado para actualizar"))
            }
        }
    }
}
