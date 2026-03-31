package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * iOS (APNs) specific notification configuration.
 *
 * @param headers HTTP headers for the APNs notification
 * @param payload The APNs payload
 * @param fcmOptions FCM-specific options for APNs
 */
@Serializable
data class ApnsConfig(
    val headers: Map<String, String>? = null,
    val payload: ApnsPayload? = null,
    @SerialName("fcm_options")
    val fcmOptions: ApnsFcmOptions? = null,
)
