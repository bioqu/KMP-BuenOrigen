package cl.uchile.dcc.mobile.buenorigen.ui

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}