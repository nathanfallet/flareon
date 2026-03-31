package me.nathanfallet.flareon.core.credentials

import dev.whyoleg.cryptography.CryptographyProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import me.nathanfallet.flareon.core.crypto.CryptographyProviderFactory
import me.nathanfallet.flareon.core.http.HttpClientFactory

/**
 * Google credentials for authenticating Firebase Admin SDK requests.
 * Handles OAuth2 token fetching, caching, and automatic refresh using service account credentials.
 *
 * This mirrors the approach used by the Firebase Admin Java SDK's GoogleCredentials.
 */
class GoogleCredentials(
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
    private val cryptographyProvider: CryptographyProvider = CryptographyProviderFactory.getDefault(),
) {
    private val jwtBuilder = JwtBuilder(cryptographyProvider)
    private val mutex = Mutex()

    private var cachedToken: String? = null
    private var tokenExpirationTime: Long = 0

    companion object {
        private const val FIREBASE_SCOPES = "https://www.googleapis.com/auth/firebase.messaging"
        private const val TOKEN_EXPIRATION_BUFFER_SECONDS = 300 // Refresh 5 minutes early

        /**
         * Creates GoogleCredentials from a JSON string containing service account credentials.
         *
         * @param serviceAccountJson The service account JSON string
         * @param httpClient The Ktor HTTP client (defaults to a new client)
         * @param cryptographyProvider The cryptography provider (defaults to platform-specific provider)
         * @return A new GoogleCredentials instance
         */
        fun fromJson(
            serviceAccountJson: String,
            httpClient: HttpClient = HttpClientFactory.createClient(),
            cryptographyProvider: CryptographyProvider = CryptographyProviderFactory.getDefault(),
        ): GoogleCredentials {
            val credentials = Json.decodeFromString<ServiceAccountCredentials>(serviceAccountJson)
            return GoogleCredentials(credentials, httpClient, cryptographyProvider)
        }
    }

    /**
     * Gets a valid OAuth2 access token, fetching a new one if necessary.
     * Tokens are cached and automatically refreshed when they expire.
     *
     * @return A valid OAuth2 access token
     */
    suspend fun getAccessToken(): String = mutex.withLock {
        val now = Clock.System.now().epochSeconds

        // Check if we have a valid cached token
        if (cachedToken != null && now < tokenExpirationTime) {
            return@withLock cachedToken!!
        }

        // Fetch a new token
        val token = fetchAccessToken()
        cachedToken = token.accessToken
        tokenExpirationTime = now + token.expiresIn - TOKEN_EXPIRATION_BUFFER_SECONDS

        return@withLock token.accessToken
    }

    /**
     * Clears the cached token, forcing a refresh on the next call to getAccessToken().
     */
    fun clearCache() {
        cachedToken = null
        tokenExpirationTime = 0
    }

    /**
     * Fetches a new OAuth2 access token from Google's token endpoint.
     *
     * @return The OAuth2 token response
     */
    private suspend fun fetchAccessToken(): OAuth2TokenResponse {
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
}
