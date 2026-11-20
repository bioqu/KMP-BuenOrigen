package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.model.Cart
import cl.uchile.dcc.mobile.buenorigen.data.model.CartItem
import cl.uchile.dcc.mobile.buenorigen.data.model.Product

interface CartRepository {
    suspend fun getCart(customerId: String): Result<Cart>
    suspend fun addToCart(customerId: String, product: Product, quantity: Int): Result<Cart>
    suspend fun updateQuantity(customerId: String, productId: Long, quantity: Int): Result<Cart>
    suspend fun removeFromCart(customerId: String, productId: Long): Result<Cart>
    suspend fun clearCart(customerId: String): Result<Cart>
}

class CartRepositoryImpl : CartRepository {
    companion object {
        private val carts = mutableMapOf<String, Cart>()
    }

    override suspend fun getCart(customerId: String): Result<Cart> {
        val cart = carts.getOrPut(customerId) { Cart(id = "cart-$customerId", customerId = customerId, items = emptyList()) }
        return Result.success(cart)
    }

    override suspend fun addToCart(customerId: String, product: Product, quantity: Int): Result<Cart> {
        if (!product.isActive || product.stock <= 0) {
            return Result.failure(Exception("El producto no está disponible o sin stock"))
        }
        val currentCart = carts.getOrPut(customerId) { Cart(id = "cart-$customerId", customerId = customerId, items = emptyList()) }
        val existingItem = currentCart.items.find { it.productId == product.id }

        val newQuantity = (existingItem?.quantity ?: 0) + quantity
        if (newQuantity > 3) {
            return Result.failure(Exception("Máximo 3 unidades permitidas del mismo producto en el carrito"))
        }
        if (newQuantity > product.stock) {
            return Result.failure(Exception("La cantidad supera el stock disponible (${product.stock})"))
        }

        val unitPrice = product.discountPrice ?: product.price
        val updatedItems = if (existingItem != null) {
            currentCart.items.map { if (it.productId == product.id) it.copy(quantity = newQuantity) else it }
        } else {
            currentCart.items + CartItem(id = "item-${product.id}", productId = product.id, product = product, quantity = newQuantity, unitPrice = unitPrice)
        }

        val updatedCart = currentCart.copy(items = updatedItems)
        carts[customerId] = updatedCart
        return Result.success(updatedCart)
    }

    override suspend fun updateQuantity(customerId: String, productId: Long, quantity: Int): Result<Cart> {
        val currentCart = carts[customerId] ?: return Result.failure(Exception("Carrito no encontrado"))
        if (quantity <= 0) {
            return removeFromCart(customerId, productId)
        }
        if (quantity > 3) {
            return Result.failure(Exception("Máximo 3 unidades permitidas"))
        }

        val item = currentCart.items.find { it.productId == productId } ?: return Result.failure(Exception("Item no en el carrito"))
        if (quantity > item.product.stock) {
            return Result.failure(Exception("Cantidad supera el stock disponible"))
        }

        val updatedItems = currentCart.items.map { if (it.productId == productId) it.copy(quantity = quantity) else it }
        val updatedCart = currentCart.copy(items = updatedItems)
        carts[customerId] = updatedCart
        return Result.success(updatedCart)
    }

    override suspend fun removeFromCart(customerId: String, productId: Long): Result<Cart> {
        val currentCart = carts[customerId] ?: return Result.failure(Exception("Carrito no encontrado"))
        val updatedItems = currentCart.items.filter { it.productId != productId }
        val updatedCart = currentCart.copy(items = updatedItems)
        carts[customerId] = updatedCart
        return Result.success(updatedCart)
    }

    override suspend fun clearCart(customerId: String): Result<Cart> {
        val currentCart = carts[customerId] ?: return Result.failure(Exception("Carrito no encontrado"))
        val updatedCart = currentCart.copy(items = emptyList())
        carts[customerId] = updatedCart
        return Result.success(updatedCart)
    }
}
