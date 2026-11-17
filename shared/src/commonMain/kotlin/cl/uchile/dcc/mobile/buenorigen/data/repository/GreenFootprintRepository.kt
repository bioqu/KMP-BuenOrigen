package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.model.GreenFootprintSummary
import cl.uchile.dcc.mobile.buenorigen.data.model.GreenPointMovement
import cl.uchile.dcc.mobile.buenorigen.data.model.MovementType

interface GreenFootprintRepository {
    suspend fun getGreenFootprint(customerId: String): Result<GreenFootprintSummary>
}

class GreenFootprintRepositoryImpl : GreenFootprintRepository {
    private val summaries = mutableMapOf<String, GreenFootprintSummary>()

    override suspend fun getGreenFootprint(customerId: String): Result<GreenFootprintSummary> {
        val summary = summaries.getOrPut(customerId) {
            GreenFootprintSummary(
                customerId = customerId,
                totalGreenPointsBalance = 80, // 80 puntos de bienvenida (RN-081)
                totalCo2EvitadoKg = 0.0,
                movements = listOf(
                    GreenPointMovement(
                        id = "mov-welcome",
                        customerId = customerId,
                        points = 80,
                        type = MovementType.WELCOME_BONUS,
                        description = "Bono de bienvenida EcoTienda",
                        createdAt = "2025-05-18"
                    )
                )
            )
        }
        return Result.success(summary)
    }
}
