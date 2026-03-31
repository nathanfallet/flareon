package me.nathanfallet.flareon.core.credentials

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from Google OAuth2 token endpoint.
 */
@Serializable
data class OAuth2TokenResponse(
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("token_type")
    val tokenType: String,

    @SerialName("expires_in")
    val expiresIn: Int,
)
