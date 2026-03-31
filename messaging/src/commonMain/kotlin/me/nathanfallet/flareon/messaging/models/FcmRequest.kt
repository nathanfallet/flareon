package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.Serializable

/**
 * The root request object sent to the FCM HTTP v1 API.
 * This wraps the actual message.
 *
 * @param message The FCM message to send
 */
@Serializable
data class FcmRequest(
    val message: FcmMessage,
)
