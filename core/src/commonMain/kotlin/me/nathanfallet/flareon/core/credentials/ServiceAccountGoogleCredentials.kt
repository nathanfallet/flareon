package me.nathanfallet.flareon.core.credentials

import dev.whyoleg.cryptography.CryptographyProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.nathanfallet.flareon.core.crypto.CryptographyProviderFactory
import me.nathanfallet.flareon.core.http.HttpClientFactory

/**
 * Google credentials backed by a service account JSON key: a signed JWT is exchanged
 * for an OAuth2 access token.
 */
class ServiceAccountGoogleCredentials(
    /**
     * The Firebase service account credentials
     */
    val serviceAccount: ServiceAccountCredentials,
    /**
     * The Ktor HTTP client for making requests (defaults to a new client)
     */
    private val httpClient: HttpClient = HttpClientFactory.createClient(),
    /**
     * The cryptography provider for JWT signing (defaults to platform-specific provider)
     */
    cryptographyProvider: CryptographyProvider = CryptographyProviderFactory.getDefault(),
) : GoogleCredentials() {
    private val jwtBuilder = JwtBuilder(cryptographyProvider)

    override suspend fun fetchAccessToken(): OAuth2TokenResponse {
        // Create JWT assertion
        val assertion = jwtBuilder.createAssertion(
            credentials = serviceAccount,
            scopes = listOf(FIREBASE_SCOPES)
        )

        // Exchange JWT for access token
        val response = httpClient.post(serviceAccount.tokenUri) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$assertion"
            )
        }

        return response.body<OAuth2TokenResponse>()
    }

    override suspend fun fetchProjectId(): String = serviceAccount.projectId
}
