package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Web Push notification options.
 *
 * @param title Notification title
 * @param body Notification body text
 * @param icon Notification icon URL
 * @param badge Notification badge URL
 * @param image Notification image URL
 * @param data Custom data for the notification
 * @param dir Text direction (ltr, rtl, auto)
 * @param lang Language code
 * @param tag Notification tag for replacement
 * @param requireInteraction Whether notification requires user interaction
 * @param renotify Whether to renotify on tag replacement
 * @param silent Whether notification is silent
 * @param timestamp Notification timestamp in milliseconds
 * @param vibrate Vibration pattern
 */
@Serializable
data class WebpushNotification(
    val title: String? = null,
    val body: String? = null,
    val icon: String? = null,
    val badge: String? = null,
    val image: String? = null,
    val data: Map<String, String>? = null,
    val dir: String? = null,
    val lang: String? = null,
    val tag: String? = null,
    @SerialName("require_interaction")
    val requireInteraction: Boolean? = null,
    val renotify: Boolean? = null,
    val silent: Boolean? = null,
    val timestamp: Long? = null,
    val vibrate: List<Int>? = null,
)
