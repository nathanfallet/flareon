package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * FCM-specific options for messages.
 *
 * @param analyticsLabel Analytics label for message tracking
 */
@Serializable
data class FcmOptions(
    @SerialName("analytics_label")
    val analyticsLabel: String? = null,
)
