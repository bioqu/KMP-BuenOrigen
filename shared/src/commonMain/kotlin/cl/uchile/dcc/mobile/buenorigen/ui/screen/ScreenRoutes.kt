package cl.uchile.dcc.mobile.buenorigen.ui.screen

sealed class ScreenRoutes(val route: String, val name: String) {
    object Home : ScreenRoutes("home", "Inicio")
    object Cart : ScreenRoutes("cart", "Carrito")
    object Catalog : ScreenRoutes("catalog", "Catálogo")
    object Emprendedores: ScreenRoutes("emprendedores", "Emprendedores")
}