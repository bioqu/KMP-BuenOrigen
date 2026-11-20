package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.model.Address
import cl.uchile.dcc.mobile.buenorigen.data.model.Order
import cl.uchile.dcc.mobile.buenorigen.data.model.OrderDetail
import cl.uchile.dcc.mobile.buenorigen.data.model.OrderStatus

interface OrderRepository {
    suspend fun confirmOrder(customerId: String, address: Address, pointsToUse: Int): Result<Order>
    suspend fun getOrdersByCustomer(customerId: String): Result<List<Order>>
    suspend fun getOrderById(orderId: String): Result<Order>
    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<Order>
}

class OrderRepositoryImpl(
    private val cartRepository: CartRepository
) : OrderRepository {
    companion object {
        private val orders = mutableMapOf<String, Order>()
    }

    override suspend fun confirmOrder(customerId: String, address: Address, pointsToUse: Int): Result<Order> {
        val cartResult = cartRepository.getCart(customerId)
        if (cartResult.isFailure) return Result.failure(cartResult.exceptionOrNull() ?: Exception("Error al obtener carrito"))
        val cart = cartResult.getOrThrow()

        if (cart.items.isEmpty()) {
            return Result.failure(Exception("El carrito está vacío"))
        }

        // Verificar stock y disponibilidad de nuevo (RN-060, RN-155)
        for (item in cart.items) {
            if (!item.product.isActive || item.product.stock < item.quantity) {
                return Result.failure(Exception("Producto ${item.product.name} sin stock suficiente o inactivo"))
            }
        }

        val subtotal = cart.subtotal
        // Max 20% discount with green points (RN-089: 1 point = 1 peso, max 20% of subtotal)
        val maxPointsAllowed = (subtotal * 0.20).toInt()
        val validPoints = pointsToUse.coerceIn(0, maxPointsAllowed)
        val finalAmount = subtotal - validPoints

        val details = cart.items.map { item ->
            OrderDetail(
                id = "detail-${item.productId}",
                productId = item.productId,
                productName = item.product.name,
                unitPrice = item.unitPrice,
                quantity = item.quantity,
                historicalCo2Unit = item.product.co2EvitadoKg,
                historicalGreenPointsUnit = item.product.greenPoints
            )
        }

        val order = Order(
            id = "order-${DateTimeUtils.currentTimeMillis()}",
            customerId = customerId,
            address = address,
            items = details,
            totalAmount = subtotal,
            status = OrderStatus.CONFIRMED,
            createdAt = "2025-05-18",
            pointsUsedForDiscount = validPoints,
            finalAmountPaid = finalAmount
        )

        orders[order.id] = order
        // Vaciar carrito (RN-061)
        cartRepository.clearCart(customerId)

        return Result.success(order)
    }

    override suspend fun getOrdersByCustomer(customerId: String): Result<List<Order>> {
        return Result.success(orders.values.filter { it.customerId == customerId })
    }

    override suspend fun getOrderById(orderId: String): Result<Order> {
        val order = orders[orderId]
        return if (order != null) Result.success(order) else Result.failure(Exception("Pedido no encontrado"))
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<Order> {
        val order = orders[orderId] ?: return Result.failure(Exception("Pedido no encontrado"))
        val updated = order.copy(status = newStatus)
        orders[orderId] = updated
        return Result.success(updated)
    }
}

// Utility for mock id generation
object DateTimeUtils {
    fun currentTimeMillis(): Long = 1716000000000L // Mock timestamp
}
