package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.Serializable

/**
 * Represents the notification payload in an FCM message.
 * This is the visible notification shown to the user.
 *
 * @param title The notification title
 * @param body The notification body text
 * @param image Optional URL of an image to display in the notification
 */
@Serializable
data class FcmNotification(
    val title: String? = null,
    val body: String? = null,
    val image: String? = null,
)
