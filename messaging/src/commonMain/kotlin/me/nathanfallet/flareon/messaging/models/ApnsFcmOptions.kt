package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * FCM-specific options for APNs messages.
 *
 * @param analyticsLabel Analytics label for message tracking
 * @param image Image URL for rich notifications
 */
@Serializable
data class ApnsFcmOptions(
    @SerialName("analytics_label")
    val analyticsLabel: String? = null,
    val image: String? = null,
)
