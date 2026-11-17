package cl.uchile.dcc.mobile.buenorigen.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = BuenOrigenDatabase.Schema,
            context = context,
            name = "buenorigen.db"
        )
    }
}
