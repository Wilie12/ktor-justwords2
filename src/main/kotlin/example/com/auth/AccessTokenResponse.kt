package example.com.auth

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenResponse(
    val accessToken: String
)
