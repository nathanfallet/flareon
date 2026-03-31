package me.nathanfallet.flareon.core.credentials

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the credentials from a Firebase service account JSON file.
 * These credentials are used to authenticate and obtain OAuth2 access tokens.
 */
@Serializable
data class ServiceAccountCredentials(
    @SerialName("type")
    val type: String,

    @SerialName("project_id")
    val projectId: String,

    @SerialName("private_key_id")
    val privateKeyId: String,

    @SerialName("private_key")
    val privateKey: String,

    @SerialName("client_email")
    val clientEmail: String,

    @SerialName("client_id")
    val clientId: String,

    @SerialName("auth_uri")
    val authUri: String,

    @SerialName("token_uri")
    val tokenUri: String,

    @SerialName("auth_provider_x509_cert_url")
    val authProviderCertUrl: String,

    @SerialName("client_x509_cert_url")
    val clientCertUrl: String,

    @SerialName("universe_domain")
    val universeDomain: String? = null,
)
