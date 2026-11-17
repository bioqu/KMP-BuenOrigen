package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.model.Product

interface FavoriteRepository {
    suspend fun getFavorites(customerId: String): Result<List<Product>>
    suspend fun addFavorite(customerId: String, product: Product): Result<List<Product>>
    suspend fun removeFavorite(customerId: String, productId: String): Result<List<Product>>
}

class FavoriteRepositoryImpl : FavoriteRepository {
    private val favoritesMap = mutableMapOf<String, MutableList<Product>>()

    override suspend fun getFavorites(customerId: String): Result<List<Product>> {
        val list = favoritesMap.getOrPut(customerId) { mutableListOf() }
        return Result.success(list)
    }

    override suspend fun addFavorite(customerId: String, product: Product): Result<List<Product>> {
        val list = favoritesMap.getOrPut(customerId) { mutableListOf() }
        if (list.none { it.id == product.id }) {
            list.add(product)
        }
        return Result.success(list)
    }

    override suspend fun removeFavorite(customerId: String, productId: String): Result<List<Product>> {
        val list = favoritesMap.getOrPut(customerId) { mutableListOf() }
        list.removeIf { it.id == productId }
        return Result.success(list)
    }
}
