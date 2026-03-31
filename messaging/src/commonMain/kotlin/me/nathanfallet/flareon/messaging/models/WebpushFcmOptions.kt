package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * FCM-specific options for Web Push messages.
 *
 * @param link Link to open when notification is clicked
 * @param analyticsLabel Analytics label for message tracking
 */
@Serializable
data class WebpushFcmOptions(
    val link: String? = null,
    @SerialName("analytics_label")
    val analyticsLabel: String? = null,
)
