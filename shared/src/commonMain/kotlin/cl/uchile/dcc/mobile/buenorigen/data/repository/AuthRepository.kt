package cl.uchile.dcc.mobile.buenorigen.data.repository

import cl.uchile.dcc.mobile.buenorigen.data.model.AuthResponse
import cl.uchile.dcc.mobile.buenorigen.data.model.LoginRequest
import cl.uchile.dcc.mobile.buenorigen.data.model.RegisterRequest
import cl.uchile.dcc.mobile.buenorigen.data.model.User
import cl.uchile.dcc.mobile.buenorigen.data.model.UserRole

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<AuthResponse>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User>
}

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            // Ejemplo de llamada Ktor:
            // val response: AuthResponse = httpClient.post("auth/login") { setBody(request) }.body()
            Result.success(AuthResponse("mock-jwt-token", User("1", request.email.substringBefore("@"), request.email, UserRole.CUSTOMER)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            Result.success(AuthResponse("mock-jwt-token", User("1", request.name, request.email, UserRole.CUSTOMER)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getCurrentUser(): Result<User> {
        return Result.success(User("1", "Cliente Eco", "cliente@eco.cl", UserRole.CUSTOMER))
    }
}
