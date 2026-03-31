package me.nathanfallet.flareon.messaging.models

import kotlinx.serialization.Serializable

/**
 * Response from the FCM HTTP v1 API after successfully sending a message.
 *
 * @param name The name/ID of the sent message
 */
@Serializable
data class FcmResponse(
    val name: String,
)
