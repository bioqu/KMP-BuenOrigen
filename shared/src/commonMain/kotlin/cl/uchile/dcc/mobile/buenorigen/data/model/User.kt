package cl.uchile.dcc.mobile.buenorigen.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    CUSTOMER, PRODUCER, ADMIN
}

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val isActive: Boolean = true,
    val lastName: String? = null,
    val phone: String? = null,
    val profileImageUrl: String? = null,
    val acceptedPrivacyPolicy: Boolean = true
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val acceptedPrivacyPolicy: Boolean
)
