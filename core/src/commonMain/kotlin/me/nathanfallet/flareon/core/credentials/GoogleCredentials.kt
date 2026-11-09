package me.nathanfallet.flareon.core.credentials

import dev.whyoleg.cryptography.CryptographyProvider
import io.ktor.client.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import me.nathanfallet.flareon.core.crypto.CryptographyProviderFactory
import me.nathanfallet.flareon.core.http.HttpClientFactory

/**
 * Google credentials for authenticating Firebase Admin SDK requests.
 * Handles OAuth2 token caching and automatic refresh; where the token comes from
 * is up to the implementation:
 *
 * - [ServiceAccountGoogleCredentials]: a service account JSON key (see [fromJson])
 * - [MetadataServerCredentials]: the metadata server of Google Cloud (Cloud Run, GCE, GKE...),
 *   with no key file at all
 *
 * This mirrors the approach used by the Firebase Admin Java SDK's GoogleCredentials.
 */
abstract class GoogleCredentials {
    private val mutex = Mutex()

    private var cachedToken: String? = null
    private var tokenExpirationTime: Long = 0

    private var cachedProjectId: String? = null

    /**
     * Factories for the supported credential sources.
     */
    companion object {
        internal const val FIREBASE_SCOPES = "https://www.googleapis.com/auth/firebase.messaging"
        private const val TOKEN_EXPIRATION_BUFFER_SECONDS = 300 // Refresh 5 minutes early

        /**
         * Creates GoogleCredentials from service account credentials.
         * Kept so that `GoogleCredentials(serviceAccount)` keeps compiling.
         */
        operator fun invoke(
            serviceAccount: ServiceAccountCredentials,
            httpClient: HttpClient = HttpClientFactory.createClient(),
            cryptographyProvider: CryptographyProvider = CryptographyProviderFactory.getDefault(),
        ): GoogleCredentials = ServiceAccountGoogleCredentials(serviceAccount, httpClient, cryptographyProvider)

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
        ): ServiceAccountGoogleCredentials {
            val credentials = Json.decodeFromString<ServiceAccountCredentials>(serviceAccountJson)
            return ServiceAccountGoogleCredentials(credentials, httpClient, cryptographyProvider)
        }

        /**
         * Creates GoogleCredentials that read their tokens from the metadata server of Google Cloud.
         * Only works from inside Google Cloud (Cloud Run, GCE, GKE...), where the workload runs
         * under a service account and no key file is needed.
         *
         * @param httpClient The Ktor HTTP client (defaults to a new client)
         * @param baseUrl The metadata server URL (defaults to the one of Google Cloud)
         * @param scopes Optional OAuth2 scopes to request (defaults to those of the service account)
         */
        fun fromMetadataServer(
            httpClient: HttpClient = HttpClientFactory.createClient(),
            baseUrl: String = MetadataServerCredentials.DEFAULT_BASE_URL,
            scopes: List<String>? = null,
        ): MetadataServerCredentials = MetadataServerCredentials(httpClient, baseUrl, scopes)
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
        cachedToken?.let { token ->
            if (now < tokenExpirationTime) return@withLock token
        }

        // Fetch a new token
        val token = fetchAccessToken()
        cachedToken = token.accessToken
        tokenExpirationTime = now + token.expiresIn - TOKEN_EXPIRATION_BUFFER_SECONDS

        return@withLock token.accessToken
    }

    /**
     * Gets the ID of the Google Cloud / Firebase project these credentials belong to.
     * The value is cached once it has been read.
     */
    suspend fun getProjectId(): String = mutex.withLock {
        cachedProjectId ?: fetchProjectId().also { cachedProjectId = it }
    }

    /**
     * Clears the cached token, forcing a refresh on the next call to getAccessToken().
     */
    fun clearCache() {
        cachedToken = null
        tokenExpirationTime = 0
    }

    /**
     * Fetches a new OAuth2 access token from the source of these credentials.
     */
    protected abstract suspend fun fetchAccessToken(): OAuth2TokenResponse

    /**
     * Reads the project ID from the source of these credentials.
     */
    protected abstract suspend fun fetchProjectId(): String
}
