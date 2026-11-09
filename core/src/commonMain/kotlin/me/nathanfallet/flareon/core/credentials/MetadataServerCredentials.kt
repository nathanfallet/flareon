package me.nathanfallet.flareon.core.credentials

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import me.nathanfallet.flareon.core.http.HttpClientFactory

/**
 * Google credentials that read OAuth2 tokens from the metadata server of Google Cloud.
 *
 * On Cloud Run, Compute Engine or GKE the workload runs under a service account, and the
 * metadata server hands out short-lived tokens for it: no service account key to store,
 * rotate or leak. Grant that service account only the roles it needs (for FCM, the ability
 * to send messages) and use these credentials in place of [ServiceAccountGoogleCredentials].
 *
 * The metadata server is only reachable from inside Google Cloud, over plain HTTP.
 */
class MetadataServerCredentials(
    /**
     * The Ktor HTTP client for making requests (defaults to a new client)
     */
    private val httpClient: HttpClient = HttpClientFactory.createClient(),
    /**
     * The metadata server URL, without trailing slash
     */
    baseUrl: String = DEFAULT_BASE_URL,
    /**
     * Optional OAuth2 scopes to request; when null, the metadata server uses the scopes of
     * the service account (on Cloud Run, `cloud-platform`).
     */
    private val scopes: List<String>? = null,
) : GoogleCredentials() {
    /**
     * Constants of the metadata server.
     */
    companion object {
        /**
         * The metadata server of Google Cloud, reachable from inside Google Cloud only.
         */
        const val DEFAULT_BASE_URL = "http://metadata.google.internal"
        private const val ROOT_PATH = "/computeMetadata/v1"
    }

    private val baseUrl = baseUrl.trimEnd('/')

    override suspend fun fetchAccessToken(): OAuth2TokenResponse {
        val response = httpClient.get("$baseUrl$ROOT_PATH/instance/service-accounts/default/token") {
            header("Metadata-Flavor", "Google")
            scopes?.takeIf { it.isNotEmpty() }?.let { parameter("scopes", it.joinToString(",")) }
        }
        return response.body<OAuth2TokenResponse>()
    }

    override suspend fun fetchProjectId(): String {
        val response = httpClient.get("$baseUrl$ROOT_PATH/project/project-id") {
            header("Metadata-Flavor", "Google")
        }
        return response.bodyAsText().trim()
    }
}
