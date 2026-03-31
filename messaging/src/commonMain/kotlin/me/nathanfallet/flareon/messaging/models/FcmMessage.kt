package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a complete FCM message to be sent to a device or topic.
 * Note: Exactly one of token, topic, or condition must be specified.
 *
 * @param token The registration token of the device to target
 * @param topic The topic name to send to (e.g., "weather")
 * @param condition The condition expression for targeting (e.g., "'TopicA' in topics && 'TopicB' in topics")
 * @param notification Optional notification payload
 * @param data Optional custom data payload (key-value pairs)
 * @param android Optional Android-specific options
 * @param apns Optional iOS (APNs) specific options
 * @param webpush Optional Web Push specific options
 * @param fcmOptions Optional FCM-specific options
 */
@Serializable
data class FcmMessage(
    val token: String? = null,
    val topic: String? = null,
    val condition: String? = null,
    val notification: FcmNotification? = null,
    val data: Map<String, String>? = null,
    val android: AndroidConfig? = null,
    val apns: ApnsConfig? = null,
    val webpush: WebpushConfig? = null,
    @SerialName("fcm_options")
    val fcmOptions: FcmOptions? = null,
)
