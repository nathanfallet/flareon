package me.nathanfallet.flareon.core.http

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured Ktor HTTP clients.
 * Provides sensible defaults for JSON serialization and logging.
 */
object HttpClientFactory {
    /**
     * Creates a new HTTP client with default configuration.
     *
     * @param enableLogging Whether to enable HTTP request/response logging
     * @param logLevel The logging level (default: INFO)
     * @param json Custom JSON configuration (optional)
     * @return A configured HttpClient instance
     */
    fun createClient(
        enableLogging: Boolean = false,
        logLevel: LogLevel = LogLevel.INFO,
        json: Json = Json {
            ignoreUnknownKeys = true
            prettyPrint = false
            isLenient = true
        },
    ): HttpClient {
        return HttpClient {
            // Configure JSON serialization
            install(ContentNegotiation) {
                json(json)
            }

            // Configure logging if enabled
            if (enableLogging) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = logLevel
                }
            }

            // Configure default request settings
            expectSuccess = true
        }
    }
}
