package digital.guimauve.flareon.messaging

import digital.guimauve.flareon.core.credentials.GoogleCredentials
import digital.guimauve.flareon.core.http.HttpClientFactory
import digital.guimauve.flareon.messaging.models.FcmMessage
import digital.guimauve.flareon.messaging.models.FcmNotification
import digital.guimauve.flareon.messaging.models.FcmRequest
import digital.guimauve.flareon.messaging.models.FcmResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Service for sending Firebase Cloud Messaging (FCM) notifications.
 * Uses the FCM HTTP v1 API for sending messages to devices.
 *
 * @param projectId The Firebase project ID
 * @param httpClient The Ktor HTTP client for making requests (defaults to a new client)
 * @param credentials The Google credentials for obtaining access tokens (defaults to provided credentials)
 */
class FcmService(
    private val credentials: GoogleCredentials,
    private val projectId: String = credentials.serviceAccount.projectId,
    private val httpClient: HttpClient = HttpClientFactory.createClient(),
) {
    companion object {
        private const val FCM_API_URL = "https://fcm.googleapis.com/v1"
    }

    /**
     * Sends a notification to a specific device token.
     *
     * @param token The device registration token
     * @param title The notification title
     * @param body The notification body text
     * @param data Optional custom data payload
     * @return The FCM response containing the message ID
     */
    suspend fun sendNotification(
        token: String,
        title: String,
        body: String,
        data: Map<String, String>? = null,
    ): FcmResponse {
        val message = FcmMessage(
            token = token,
            notification = FcmNotification(
                title = title,
                body = body
            ),
            data = data
        )
        return sendMessage(message)
    }

    /**
     * Sends a custom FCM message with full control over the message structure.
     *
     * @param message The FCM message to send
     * @return The FCM response containing the message ID
     */
    suspend fun sendMessage(message: FcmMessage): FcmResponse {
        val accessToken = credentials.getAccessToken()
        val url = "$FCM_API_URL/projects/$projectId/messages:send"

        val request = FcmRequest(message = message)

        val response = httpClient.post(url) {
            bearerAuth(accessToken)
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        return response.body<FcmResponse>()
    }

    /**
     * Sends notifications to multiple device tokens in parallel.
     * Note: For truly large batches, consider using FCM's batch API or topic messaging.
     *
     * @param tokens List of device registration tokens
     * @param title The notification title
     * @param body The notification body text
     * @param data Optional custom data payload
     * @return List of FCM responses (one per token)
     */
    suspend fun sendNotificationToMultiple(
        tokens: List<String>,
        title: String,
        body: String,
        data: Map<String, String>? = null,
    ): List<Result<FcmResponse>> {
        return tokens.map { token ->
            runCatching {
                sendNotification(token, title, body, data)
            }
        }
    }
}
