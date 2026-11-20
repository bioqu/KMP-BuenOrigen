package cl.uchile.dcc.mobile.buenorigen.ui.screen

sealed class ScreenRoutes(val route: String, val name: String) {
    object Home : ScreenRoutes("home", "Inicio")
    object Cart : ScreenRoutes("cart", "Carrito")
    object Catalog : ScreenRoutes("catalog", "Catálogo")
    object Emprendedores: ScreenRoutes("emprendedores", "Emprendedores")
    object ProductDetail : ScreenRoutes("product_detail/{productId}", "Detalle de Producto") {
        fun createRoute(productId: Long) = "product_detail/$productId"
    }
    object OrderSuccess : ScreenRoutes("order_success/{orderId}", "Compra Exitosa") {
        fun createRoute(orderId: String) = "order_success/$orderId"
    }
}