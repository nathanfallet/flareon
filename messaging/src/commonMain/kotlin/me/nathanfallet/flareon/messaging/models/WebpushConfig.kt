package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Web Push protocol specific configuration.
 *
 * @param headers HTTP headers for the Web Push notification
 * @param data Data payload as key-value pairs
 * @param notification Web Push notification options
 * @param fcmOptions FCM-specific options for Web Push
 */
@Serializable
data class WebpushConfig(
    val headers: Map<String, String>? = null,
    val data: Map<String, String>? = null,
    val notification: WebpushNotification? = null,
    @SerialName("fcm_options")
    val fcmOptions: WebpushFcmOptions? = null,
)
